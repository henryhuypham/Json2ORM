package com.huyphamthanh8290.huyorm.model.viewModel;

import com.huyphamthanh8290.huyorm.orm.annotation.sql.Column;
import com.huyphamthanh8290.huyorm.orm.annotation.sql.Table;

@Table(name = "business_center")
public class BusinessCentreItem {

    @Column(name = "id", table = "business_center")
    private Integer id;

    @Column(name = "city_id", table = "business_center")
    private Integer		cityId;

    @Column(name = "name", table = "business_center")
    private String	name;

    @Column(name = "tel", table = "business_center")
    private String	tel;

    public Integer getId() {
        return id;
    }

    public Integer getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }
}