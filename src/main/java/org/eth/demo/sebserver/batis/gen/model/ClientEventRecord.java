package org.eth.demo.sebserver.batis.gen.model;

import java.math.BigDecimal;
import javax.annotation.Generated;

public class ClientEventRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.exam_id")
    private Long examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.client_id")
    private Long clientId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.type")
    private Integer type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.timestamp")
    private Long timestamp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.numeric_value")
    private BigDecimal numericValue;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.593+02:00", comments="Source field: client_event.text")
    private String text;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source Table: client_event")
    public ClientEventRecord(Long id, Long examId, Long clientId, Integer type, Long timestamp, BigDecimal numericValue, String text) {
        this.id = id;
        this.examId = examId;
        this.clientId = clientId;
        this.type = type;
        this.timestamp = timestamp;
        this.numericValue = numericValue;
        this.text = text;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.exam_id")
    public Long getExamId() {
        return examId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.client_id")
    public Long getClientId() {
        return clientId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.type")
    public Integer getType() {
        return type;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.591+02:00", comments="Source field: client_event.numeric_value")
    public BigDecimal getNumericValue() {
        return numericValue;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.593+02:00", comments="Source field: client_event.text")
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table client_event
     *
     * @mbg.generated Wed Jul 25 15:07:26 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", examId=").append(examId);
        sb.append(", clientId=").append(clientId);
        sb.append(", type=").append(type);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", numericValue=").append(numericValue);
        sb.append(", text=").append(text);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table client_event
     *
     * @mbg.generated Wed Jul 25 15:07:26 CEST 2018
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
        ClientEventRecord other = (ClientEventRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExamId() == null ? other.getExamId() == null : this.getExamId().equals(other.getExamId()))
            && (this.getClientId() == null ? other.getClientId() == null : this.getClientId().equals(other.getClientId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getNumericValue() == null ? other.getNumericValue() == null : this.getNumericValue().equals(other.getNumericValue()))
            && (this.getText() == null ? other.getText() == null : this.getText().equals(other.getText()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table client_event
     *
     * @mbg.generated Wed Jul 25 15:07:26 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamId() == null) ? 0 : getExamId().hashCode());
        result = prime * result + ((getClientId() == null) ? 0 : getClientId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getNumericValue() == null) ? 0 : getNumericValue().hashCode());
        result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
        return result;
    }
}