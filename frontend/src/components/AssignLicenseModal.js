import React, { useState, useEffect } from 'react';
import { Modal, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { deviceService, licenseService, assignmentService } from '../services/api';

const AssignLicenseModal = ({ deviceId, onClose }) => {
    const [licenses, setLicenses] = useState([]);
    const [device, setDevice] = useState(null);
    const [selectedLicenseKey, setSelectedLicenseKey] = useState('');
    const [usageInfo, setUsageInfo] = useState({ current: 0, max: null });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [isAssigned, setIsAssigned] = useState(false); 

    const loadData = async () => {
        setLoading(true);
        setError('');
        try {
            const deviceRes = await deviceService.getDeviceById(deviceId); 
            setDevice(deviceRes.data);

            const licensesRes = await licenseService.getAllLicenses();
            setLicenses(licensesRes.data);

        } catch (err) {
            console.error(err);
            const errorMessage = err.response?.data?.message || 'Failed to load required data.';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };
    
    useEffect(() => {
        if (!isAssigned) {
             loadData();
        }
    }, [deviceId, isAssigned]);

    useEffect(() => {
        if (selectedLicenseKey) {
            const loadUsage = async () => {
                const license = licenses.find(l => l.licenseKey === selectedLicenseKey);
                try {
                    const usageRes = await assignmentService.getLicenseUsage(selectedLicenseKey);
                    setUsageInfo({
                        current: usageRes.data,
                        max: license.maxUsage
                    });
                } catch (err) {
                    setError('Failed to check license usage.');
                }
            };
            loadUsage();
        } else {
            setUsageInfo({ current: 0, max: null });
        }
    }, [selectedLicenseKey, licenses]);

    const handleAssign = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');

        if (!selectedLicenseKey) {
            setError('Please select a license to assign.');
            return;
        }
 
        if (usageInfo.max !== null && usageInfo.current >= usageInfo.max) {
             setError(`Assignment failed: Max Usage (${usageInfo.max}) Exceeded.`);
             return;
        }

        try {
            /*
            const today = new Date();
const year = today.getFullYear();
const month = String(today.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed, so add 1
const day = String(today.getDate()).padStart(2, '0');
const formattedDate = `${year}-${month}-${day}`;
console.log(formattedDate);
*/
            let data = {
            assignmentId: "123",
            deviceId: deviceId,
            licenseId: selectedLicenseKey,
            assignmentDate: "2025-08-10",
            }
            
            console.log(data);
            await assignmentService.assignLicense(data);

            setMessage('License Assignment Successful! 🎉');
            setIsAssigned(true);
            
        } catch (err) {
            console.log(err)
            const msg = err.response?.data?.message || 'Failed to assign license (Unknown Error).';
            setError(msg);
        }
    };

    // The Unassign logic and related state/calls are now completely irrelevant 
    // since the table showing assignments is removed. You can remove the function 
    // entirely or just leave it out to simplify. The user won't be able to see 
    // or unassign licenses from this modal anymore.

    const getUtilizationPercent = (current, max) => {
        if (max === null) return 'N/A (Unlimited)';
        if (max === 0) return `${current}/0 (0%)`;
        return `${current}/${max} (${((current / max) * 100).toFixed(0)}%)`;
    };

    return (
        <Modal show onHide={onClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Assign License to Device: **{device?.deviceId || deviceId}**</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                {message && <Alert variant="success">{message}</Alert>}

                {loading ? (
                    <div className="text-center"><Spinner animation="border" /></div>
                ) : isAssigned ? (
                    <div className="text-center p-5">
                        <p className="lead fw-bold">Assignment Complete!</p>
                        <Button variant="primary" onClick={onClose}>
                            Close and return to Dashboard
                        </Button>
                    </div>
                ) : (
                    <>
                        <Form onSubmit={handleAssign} className="mb-4 p-3 border rounded">
                            <Form.Group className="mb-3">
                                <Form.Label>Select License to Assign</Form.Label>
                                <Form.Select
                                    value={selectedLicenseKey}
                                    onChange={(e) => setSelectedLicenseKey(e.target.value)}
                                    required
                                >
                                    <option value="">-- Choose License --</option>
                                    {licenses.map(l => (
                                        <option key={l.licenseKey} value={l.licenseKey}>
                                            {l.softwareName} ({l.licenseKey})
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            {selectedLicenseKey && (
                                <div className="mb-3">
                                    <p className={`text-${usageInfo.max !== null && usageInfo.current >= usageInfo.max ? 'danger' : 'success'} fw-bold`}>
                                        License Utilization: {getUtilizationPercent(usageInfo.current, usageInfo.max)}
                                    </p>
                                    {usageInfo.max !== null && usageInfo.current >= usageInfo.max && (
                                        <Alert variant="warning" className="p-2">
                                            ⚠️ This license is at or over capacity. Assignment will fail.
                                        </Alert>
                                    )}
                                </div>
                            )}

                            <Button 
                                variant="success" 
                                type="submit"
                                disabled={!selectedLicenseKey || (usageInfo.max !== null && usageInfo.current >= usageInfo.max)}
                            >
                                Assign License
                            </Button>
                        </Form>
                    </>
                )}
            </Modal.Body>
        </Modal>
    );
};

export default AssignLicenseModal;