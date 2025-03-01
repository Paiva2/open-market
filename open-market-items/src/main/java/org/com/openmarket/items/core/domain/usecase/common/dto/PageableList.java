package org.com.openmarket.items.core.domain.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableList<T> {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean isLast;
    private List<T> data;
}
