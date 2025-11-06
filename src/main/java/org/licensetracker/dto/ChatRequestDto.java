package org.licensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {

    // The field name 'message' corresponds to how the controller template retrieves the query.
    private String message;

    // You could rename this to 'query' if preferred, but then you must update the controller logic.
}
