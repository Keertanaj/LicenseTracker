import React, { useState, useEffect } from 'react';
import { Button, Table, Form, Row, Col } from 'react-bootstrap';
import { CSVLink } from 'react-csv'; 
import jsPDF from 'jspdf'; 
import autoTable from 'jspdf-autotable';
import { reportService } from '../services/api'; 

const ReportsPage = () => {
    const [reportData, setReportData] = useState([]);
    const [filters, setFilters] = useState({ vendor: '', software: '', location: '' });
    const [loading, setLoading] = useState(false);

    // State to hold dropdown options, derived from reportData
    const [vendorOptions, setVendorOptions] = useState([]);
    const [softwareOptions, setSoftwareOptions] = useState([]);
    const [locationOptions, setLocationOptions] = useState([]);

    const fetchReport = async (currentFilters) => {
        setLoading(true);
        try {
            const activeFilters = {};
            for (const key in currentFilters) {
                if (currentFilters[key]) {
                    activeFilters[key] = currentFilters[key];
                }
            }
            const queryString = new URLSearchParams(activeFilters).toString();
            const res = await reportService.getLicenseReport(queryString);
            setReportData(res.data || []);
        } catch (error) {
            console.error("Failed to fetch report data:", error);
            setReportData([]);
        } finally {
            setLoading(false);
        }
    };

    // Initial data fetch
    useEffect(() => {
        fetchReport(filters);
    }, []);

    // When reportData updates, derive the filter options from it
    useEffect(() => {
        if (reportData.length > 0) {
            setVendorOptions([...new Set(reportData.map(item => item.vendorName))].sort());
            setSoftwareOptions([...new Set(reportData.map(item => item.softwareName))].sort());
            setLocationOptions([...new Set(reportData.map(item => item.location))].sort());
        }
    }, [reportData]);

    const handleFilterChange = (e) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleFilterSubmit = () => {
        fetchReport(filters);
    }

    const formatDataForExport = () => {
        return reportData.map(item => ({
            'License Key': item.licenseKey,
            'Device ID': item.deviceId,
            'Software': item.softwareName,
            'Vendor': item.vendorName,
            'Location': item.location,
            'Expiry': item.expiryDate,
        }));
    };
    
    const csvHeaders = [
        { label: "License Key", key: "License Key" },
        { label: "Device ID", key: "Device ID" },
        { label: "Software", key: "Software" },
        { label: "Vendor", key: "Vendor" },
        { label: "Location", key: "Location" },
        { label: "Expiry", key: "Expiry" },
    ];

    const exportPdf = () => {
        const doc = new jsPDF();
        doc.text("License Management Report", 14, 20);

        const columns = ["License Key", "Device ID", "Software", "Vendor", "Location", "Expiry"];
        const rows = reportData.map(item => [
            item.licenseKey,
            item.deviceId,
            item.softwareName,
            item.vendorName,
            item.location,
            item.expiryDate
        ]);

        autoTable(doc, {
            head: [columns],
            body: rows,
            startY: 30,
            theme: 'striped',
        });

        doc.save('license_report.pdf');
    };

    return (
        <div className="container mt-4">
            <h2>📊 Reports & Export</h2>

            <Form className="mb-3 border p-3 rounded" onSubmit={(e) => { e.preventDefault(); handleFilterSubmit(); }}>
                <Row>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label>Vendor</Form.Label>
                            <Form.Select name="vendor" value={filters.vendor} onChange={handleFilterChange}>
                                <option value="">All Vendors</option>
                                {vendorOptions.map(vendor => (
                                    <option key={vendor} value={vendor}>{vendor}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label>Software</Form.Label>
                            <Form.Select name="software" value={filters.software} onChange={handleFilterChange}>
                                <option value="">All Software</option>
                                {softwareOptions.map(software => (
                                    <option key={software} value={software}>{software}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label>Location</Form.Label>
                            <Form.Select name="location" value={filters.location} onChange={handleFilterChange}>
                                <option value="">All Locations</option>
                                {locationOptions.map(location => (
                                    <option key={location} value={location}>{location}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3} className="d-flex align-items-end">
                        <Button variant="primary" onClick={handleFilterSubmit} disabled={loading}>
                            {loading ? 'Filtering...' : 'Apply Filters'}
                        </Button>
                    </Col>
                </Row>
            </Form>

            {/* Export Buttons */}
            <div className="mb-3">
                <CSVLink
                    data={formatDataForExport()}
                    headers={csvHeaders}
                    filename={"license_report.csv"}
                    className="btn btn-success me-2"
                >
                    Export as CSV
                </CSVLink>
                <Button onClick={exportPdf} variant="danger">
                    Export as PDF
                </Button>
            </div>

            {/* Report Table */}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>License Key</th>
                        <th>Device ID</th>
                        <th>Software</th>
                        <th>Vendor</th>
                        <th>Location</th>
                        <th>Expiry</th>
                    </tr>
                </thead>
                <tbody>
                    {reportData.map((item, index) => (
                        <tr key={index}>
                            <td>{item.licenseKey}</td>
                            <td>{item.deviceId}</td>
                            <td>{item.softwareName}</td>
                            <td>{item.vendorName}</td>
                            <td>{item.location}</td>
                            <td>{item.expiryDate}</td>
                        </tr>
                    ))}
                    {reportData.length === 0 && !loading && (
                        <tr>
                            <td colSpan="6" className="text-center">No assignments found based on current filters.</td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </div>
    );
};

export default ReportsPage;