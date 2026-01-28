package com.example.library.service;

import com.example.library.dto.PageResponse;
import java.util.List;

/**
 * Helper service for pagination operations
 */
public class PaginationService {
    
    /**
     * Paginate a list of items
     */
    public static <T> PageResponse<T> paginate(List<T> allItems, int pageNumber, int pageSize) {
        if (pageNumber < 0) pageNumber = 0;
        if (pageSize <= 0) pageSize = 10;
        
        int totalItems = allItems.size();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        
        List<T> pageContent = allItems.subList(startIndex, endIndex);
        return new PageResponse<>(pageContent, pageNumber, pageSize, totalItems);
    }

    /**
     * Sort and paginate a list
     */
    public static <T extends Comparable<T>> PageResponse<T> sortAndPaginate(
            List<T> allItems, int pageNumber, int pageSize, boolean ascending) {
        allItems.sort((a, b) -> ascending ? a.compareTo(b) : b.compareTo(a));
        return paginate(allItems, pageNumber, pageSize);
    }
}
