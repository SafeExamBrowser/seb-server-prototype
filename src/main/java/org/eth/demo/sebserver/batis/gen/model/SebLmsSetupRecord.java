package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class SebLmsSetupRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.institution_id")
    private Long institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_type")
    private String lmsType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_url")
    private String lmsUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_clientname")
    private String lmsClientname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_clientsecret")
    private String lmsClientsecret;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_rest_api_token")
    private String lmsRestApiToken;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.310+01:00", comments="Source field: seb_lms_setup.seb_clientname")
    private String sebClientname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.310+01:00", comments="Source field: seb_lms_setup.seb_clientsecret")
    private String sebClientsecret;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source Table: seb_lms_setup")
    public SebLmsSetupRecord(Long id, Long institutionId, String name, String lmsType, String lmsUrl, String lmsClientname, String lmsClientsecret, String lmsRestApiToken, String sebClientname, String sebClientsecret) {
        this.id = id;
        this.institutionId = institutionId;
        this.name = name;
        this.lmsType = lmsType;
        this.lmsUrl = lmsUrl;
        this.lmsClientname = lmsClientname;
        this.lmsClientsecret = lmsClientsecret;
        this.lmsRestApiToken = lmsRestApiToken;
        this.sebClientname = sebClientname;
        this.sebClientsecret = sebClientsecret;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.institution_id")
    public Long getInstitutionId() {
        return institutionId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_type")
    public String getLmsType() {
        return lmsType;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_url")
    public String getLmsUrl() {
        return lmsUrl;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_clientname")
    public String getLmsClientname() {
        return lmsClientname;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_clientsecret")
    public String getLmsClientsecret() {
        return lmsClientsecret;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.309+01:00", comments="Source field: seb_lms_setup.lms_rest_api_token")
    public String getLmsRestApiToken() {
        return lmsRestApiToken;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.310+01:00", comments="Source field: seb_lms_setup.seb_clientname")
    public String getSebClientname() {
        return sebClientname;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-11-07T16:15:38.310+01:00", comments="Source field: seb_lms_setup.seb_clientsecret")
    public String getSebClientsecret() {
        return sebClientsecret;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seb_lms_setup
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", institutionId=").append(institutionId);
        sb.append(", name=").append(name);
        sb.append(", lmsType=").append(lmsType);
        sb.append(", lmsUrl=").append(lmsUrl);
        sb.append(", lmsClientname=").append(lmsClientname);
        sb.append(", lmsClientsecret=").append(lmsClientsecret);
        sb.append(", lmsRestApiToken=").append(lmsRestApiToken);
        sb.append(", sebClientname=").append(sebClientname);
        sb.append(", sebClientsecret=").append(sebClientsecret);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seb_lms_setup
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SebLmsSetupRecord other = (SebLmsSetupRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInstitutionId() == null ? other.getInstitutionId() == null : this.getInstitutionId().equals(other.getInstitutionId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getLmsType() == null ? other.getLmsType() == null : this.getLmsType().equals(other.getLmsType()))
            && (this.getLmsUrl() == null ? other.getLmsUrl() == null : this.getLmsUrl().equals(other.getLmsUrl()))
            && (this.getLmsClientname() == null ? other.getLmsClientname() == null : this.getLmsClientname().equals(other.getLmsClientname()))
            && (this.getLmsClientsecret() == null ? other.getLmsClientsecret() == null : this.getLmsClientsecret().equals(other.getLmsClientsecret()))
            && (this.getLmsRestApiToken() == null ? other.getLmsRestApiToken() == null : this.getLmsRestApiToken().equals(other.getLmsRestApiToken()))
            && (this.getSebClientname() == null ? other.getSebClientname() == null : this.getSebClientname().equals(other.getSebClientname()))
            && (this.getSebClientsecret() == null ? other.getSebClientsecret() == null : this.getSebClientsecret().equals(other.getSebClientsecret()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seb_lms_setup
     *
     * @mbg.generated Wed Nov 07 16:15:38 CET 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInstitutionId() == null) ? 0 : getInstitutionId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getLmsType() == null) ? 0 : getLmsType().hashCode());
        result = prime * result + ((getLmsUrl() == null) ? 0 : getLmsUrl().hashCode());
        result = prime * result + ((getLmsClientname() == null) ? 0 : getLmsClientname().hashCode());
        result = prime * result + ((getLmsClientsecret() == null) ? 0 : getLmsClientsecret().hashCode());
        result = prime * result + ((getLmsRestApiToken() == null) ? 0 : getLmsRestApiToken().hashCode());
        result = prime * result + ((getSebClientname() == null) ? 0 : getSebClientname().hashCode());
        result = prime * result + ((getSebClientsecret() == null) ? 0 : getSebClientsecret().hashCode());
        return result;
    }
}