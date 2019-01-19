package com.laputa.zeejp.lib_common.http.response;

import java.util.List;

public class FilterDataResponse {
    private List<FilterResponse> filterDetail;

    public List<FilterResponse> getFilterDetail() {
        return filterDetail;
    }

    public void setFilterDetail(List<FilterResponse> filterDetail) {
        this.filterDetail = filterDetail;
    }
}
