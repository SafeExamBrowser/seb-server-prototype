package org.eth.demo.sebserver.batis.gen.model;

import javax.annotation.Generated;

public class ExamConfigurationMapRecord {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.exam_id")
    private Long examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.configuration_id")
    private Long configurationId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.client_info")
    private String clientInfo;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source Table: exam_configuration_map")
    public ExamConfigurationMapRecord(Long id, Long examId, Long configurationId, String clientInfo) {
        this.id = id;
        this.examId = examId;
        this.configurationId = configurationId;
        this.clientInfo = clientInfo;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.exam_id")
    public Long getExamId() {
        return examId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.960+02:00", comments="Source field: exam_configuration_map.configuration_id")
    public Long getConfigurationId() {
        return configurationId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.961+02:00", comments="Source field: exam_configuration_map.client_info")
    public String getClientInfo() {
        return clientInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam_configuration_map
     *
     * @mbg.generated Fri Jun 08 16:06:32 CEST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", examId=").append(examId);
        sb.append(", configurationId=").append(configurationId);
        sb.append(", clientInfo=").append(clientInfo);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam_configuration_map
     *
     * @mbg.generated Fri Jun 08 16:06:32 CEST 2018
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
        ExamConfigurationMapRecord other = (ExamConfigurationMapRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExamId() == null ? other.getExamId() == null : this.getExamId().equals(other.getExamId()))
            && (this.getConfigurationId() == null ? other.getConfigurationId() == null : this.getConfigurationId().equals(other.getConfigurationId()))
            && (this.getClientInfo() == null ? other.getClientInfo() == null : this.getClientInfo().equals(other.getClientInfo()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam_configuration_map
     *
     * @mbg.generated Fri Jun 08 16:06:32 CEST 2018
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamId() == null) ? 0 : getExamId().hashCode());
        result = prime * result + ((getConfigurationId() == null) ? 0 : getConfigurationId().hashCode());
        result = prime * result + ((getClientInfo() == null) ? 0 : getClientInfo().hashCode());
        return result;
    }
}