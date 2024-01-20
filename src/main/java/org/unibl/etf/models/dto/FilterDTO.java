package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO
{
    private String columnName;
    private Object columnValue;
}
