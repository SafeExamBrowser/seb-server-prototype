package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;
import org.joda.time.DateTime;

public class ExamRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.492+02:00", comments="Source field: exam.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.owner_id")
    private Long ownerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.status")
    private String status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.start_time")
    private DateTime startTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.end_time")
    private DateTime endTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.lms_exam_url")
    private String lmsExamUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.492+02:00", comments="Source Table: exam")
    public ExamRecord(Long id, Long ownerId, String name, String status, DateTime startTime, DateTime endTime, String lmsExamUrl) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lmsExamUrl = lmsExamUrl;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.492+02:00", comments="Source field: exam.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.owner_id")
    public Long getOwnerId() {
        return ownerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.status")
    public String getStatus() {
        return status;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.start_time")
    public DateTime getStartTime() {
        return startTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.end_time")
    public DateTime getEndTime() {
        return endTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.493+02:00", comments="Source field: exam.lms_exam_url")
    public String getLmsExamUrl() {
        return lmsExamUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Fri Aug 03 08:27:35 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", lmsExamUrl=").append(lmsExamUrl);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Fri Aug 03 08:27:35 CEST 2018
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
        ExamRecord other = (ExamRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getLmsExamUrl() == null ? other.getLmsExamUrl() == null : this.getLmsExamUrl().equals(other.getLmsExamUrl()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbg.generated Fri Aug 03 08:27:35 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getLmsExamUrl() == null) ? 0 : getLmsExamUrl().hashCode());
        return result;
    }
}