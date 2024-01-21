package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySubscribeDTO {
    private Long id;
    private String categoryName;
    private boolean subscribed;
}
