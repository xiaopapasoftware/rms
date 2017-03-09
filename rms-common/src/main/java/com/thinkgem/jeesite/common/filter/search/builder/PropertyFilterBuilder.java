package com.thinkgem.jeesite.common.filter.search.builder;


import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangganggang on 2017/1/3.
 */
public class PropertyFilterBuilder {
    private List<PropertyFilter> propertyFilters ;

    private MatchType matchType;
    private PropertyType propertyType;

    private PropertyFilterBuilder() {
        propertyFilters = new ArrayList<>();
    }

    public static PropertyFilterBuilder create() {
        return new PropertyFilterBuilder();
    }

    public PropertyFilterBuilder matchTye(MatchType matchType) {
        this.matchType = matchType;
        return this;
    }

    public PropertyFilterBuilder propertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public PropertyFilterBuilder add(String fileName, String value) {
        PropertyFilter propertyFilter = new PropertyFilter(matchType, propertyType, fileName, value);
        propertyFilters.add(propertyFilter);
        return this;
    }

    public PropertyFilterBuilder addFilter(MatchType matchType, PropertyType propertyType, String fileName, String value) {
        PropertyFilter propertyFilter = new PropertyFilter(matchType, propertyType, fileName, value);
        propertyFilters.add(propertyFilter);
        return this;
    }

    public List<PropertyFilter> end() {
        return propertyFilters;
    }

}
