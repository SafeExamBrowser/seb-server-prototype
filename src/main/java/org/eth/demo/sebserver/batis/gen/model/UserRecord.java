package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;
import org.joda.time.DateTime;

public class UserRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.institution_id")
    private Long institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.uuid")
    private String uuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.user_name")
    private String userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.password")
    private String password;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.email")
    private String email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.creation_date")
    private DateTime creationDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.active")
    private Boolean active;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.locale")
    private String locale;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.timezone")
    private String timezone;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source Table: user")
    public UserRecord(Long id, Long institutionId, String uuid, String name, String userName, String password, String email, DateTime creationDate, Boolean active, String locale, String timezone) {
        this.id = id;
        this.institutionId = institutionId;
        this.uuid = uuid;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
        this.locale = locale;
        this.timezone = timezone;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.institution_id")
    public Long getInstitutionId() {
        return institutionId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.892+02:00", comments="Source field: user.uuid")
    public String getUuid() {
        return uuid;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.user_name")
    public String getUserName() {
        return userName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.password")
    public String getPassword() {
        return password;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.email")
    public String getEmail() {
        return email;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.creation_date")
    public DateTime getCreationDate() {
        return creationDate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.active")
    public Boolean getActive() {
        return active;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.locale")
    public String getLocale() {
        return locale;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.893+02:00", comments="Source field: user.timezone")
    public String getTimezone() {
        return timezone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Thu Oct 25 10:22:09 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", institutionId=").append(institutionId);
        sb.append(", uuid=").append(uuid);
        sb.append(", name=").append(name);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", email=").append(email);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", active=").append(active);
        sb.append(", locale=").append(locale);
        sb.append(", timezone=").append(timezone);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Thu Oct 25 10:22:09 CEST 2018
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
        UserRecord other = (UserRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInstitutionId() == null ? other.getInstitutionId() == null : this.getInstitutionId().equals(other.getInstitutionId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getCreationDate() == null ? other.getCreationDate() == null : this.getCreationDate().equals(other.getCreationDate()))
            && (this.getActive() == null ? other.getActive() == null : this.getActive().equals(other.getActive()))
            && (this.getLocale() == null ? other.getLocale() == null : this.getLocale().equals(other.getLocale()))
            && (this.getTimezone() == null ? other.getTimezone() == null : this.getTimezone().equals(other.getTimezone()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Thu Oct 25 10:22:09 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInstitutionId() == null) ? 0 : getInstitutionId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getCreationDate() == null) ? 0 : getCreationDate().hashCode());
        result = prime * result + ((getActive() == null) ? 0 : getActive().hashCode());
        result = prime * result + ((getLocale() == null) ? 0 : getLocale().hashCode());
        result = prime * result + ((getTimezone() == null) ? 0 : getTimezone().hashCode());
        return result;
    }
}