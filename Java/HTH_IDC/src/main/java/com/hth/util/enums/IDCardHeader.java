package com.hth.util.enums;

import java.util.Arrays;

/**
 * Constants represent the headers of the merged variables in ID Card.
 * 
 * @author Dickfoong
 */
public enum IDCardHeader {
    AREA(new String[]{"AREA"}),
    COINSURANCE_IN_NETWORK(new String[]{"COI", "CO1", "CO2"}),
    COINSURANCE_OUT_NETWORK((new String[]{"CO3", "CO4", "CO5"})),
    COPAY(new String[]{"COP"}),
    COPAY_ER(new String[]{"COP3"}),
    COPAY_HOSPITAL(new String[]{"COP2", "COP5"}),
    COPAY_OFFICE_VISIT(new String[]{"COP1", "COP4"}),
    COVERAGE_TYPE(new String[]{"TOC", "TOC2"}),
    DEDUCTIBLE_IN_NETWORK(new String[]{"DD", "DD1", "DD2"}),
    DEDUCTIBLE_OUT_NETWORK(new String[]{"DD3", "DD4", "DD5"}),
    DENT_COVERAGE(new String[]{"DTOC", "DTOC1", "DTOC2"}),
    DENT_PLAN(new String[]{"PLND"}),
    DEPENDENT_1(new String[]{"DF1"}),
    DEPENDENT_2(new String[]{"DF2"}),
    DEPENDENT_3(new String[]{"DF3"}),
    DEPENDENT_4(new String[]{"DF4"}),
    DEPENDENT_5(new String[]{"DF5"}),
    DEPENDENT_6(new String[]{"DF6"}),
    DEPENDENT_1_DOB(new String[]{"DB1"}),
    DEPENDENT_2_DOB(new String[]{"DB2"}),
    DEPENDENT_3_DOB(new String[]{"DB3"}),
    DEPENDENT_4_DOB(new String[]{"DB4"}),
    DEPENDENT_5_DOB(new String[]{"DB5"}),
    DEPENDENT_6_DOB(new String[]{"DB6"}),
    DEPENDENT_7_DOB(new String[]{"DB7"}),
    DEPENDENT_8_DOB(new String[]{"DB8"}),
    DEPENDENT_9_DOB(new String[]{"DB9"}),
    DEPENDENT_10_DOB(new String[]{"DB10"}),
    DEPENDENT_1_PCD(new String[]{"DP1"}),
    DEPENDENT_2_PCD(new String[]{"DP2"}),
    DEPENDENT_3_PCD(new String[]{"DP3"}),
    DEPENDENT_4_PCD(new String[]{"DP4"}),
    DEPENDENT_5_PCD(new String[]{"DP5"}),
    DEPENDENT_6_PCD(new String[]{"DP6"}),
    DEPENDENT_7_PCD(new String[]{"DP7"}),
    DEPENDENT_8_PCD(new String[]{"DP8"}),
    DEPENDENT_9_PCD(new String[]{"DP9"}),
    DEPENDENT_10_PCD(new String[]{"DP10"}),
    DEPENDENT_NAME(new String[]{"DEP"}),
    DIV_NAME(new String[]{"GRP6"}),
    DIV_NUM(new String[]{"DIV"}),
    EFF_DATE(new String[]{"EFF", "EFF3", "EFF5", "EFF6"}),
    EFF_DATE_MMM_FORMAT(new String[]{"EFF2"}),
    EFF_DATE_YYYY_FORMAT(new String[]{"EFF4"}),
    ELECTED_COUNTRY(new String[]{"ELC"}),
    EMP_DOB(new String[]{"DOB", "DOB2", "DOB3"}),
    EMP_NAME(new String[]{"ENAM", "ENAMA", "ENAM0", "ENAM2", "ENAM3", "ENAM4", "ENAM5", "ENAM6", "ENAM7", "ENAM8", "ENAM9"}),
    EMP_PCD(new String[]{"DPE"}),
    GRP_NAME(new String[]{"GRN", "GRN2", "GRN3", "GRN7", "GRP4", "GRP5"}),
    GRP_NUM(new String[]{"GR#", "GR#2", "GR#3", "GR#4", "GR#5"}),
    GRP_NUM_DIV(new String[]{"GRD"}),
    INTERNATIONAL_DOB(new String[]{"IDOB"}),
    MED_COVERAGE(new String[]{"MTOC", "MTOCX", "MTOC1", "MTOC2"}),
    MED_PLAN(new String[]{"PLN"}),
    MED_PLAN_CODE(new String[]{"PLNT"}),
    MEM_ID(new String[]{"MBR", "MBR2", "MBR5", "MBR9"}),
    MEM_ID_GGH(new String[]{"MBRA"}),
    NETWORK(new String[]{"NET"}),
    ORIG_EFF_DATE(new String[]{"ORG"}),
    PLAN_1(new String[]{"PL1"}),
    PLAN_2(new String[]{"PL2"}),
    PLAN_3(new String[]{"PL3"}),
    PLAN_4(new String[]{"PL4"}),
    PLAN_5(new String[]{"PL5"}),
    PLAN_6(new String[]{"PL6"}),
    PLAN_ID(new String[]{"PID1", "PID3"}),
    POLICY_ID(new String[]{"MBR3", "MBR4", "MBR8", "POL"}),
    POLICY_ID_9(new String[]{"MBR6"}),
    POLICY_ID_10(new String[]{"MBR7"}),
    POLICY_ID_EL_MEM_ID(new String[]{"PO2"}),
    PPO_ADD_1(new String[]{"PPOA1"}),
    PPO_ADD_2(new String[]{"PPOA2"}),
    PPO_ADD_3(new String[]{"PPOA3"}),
    PPO_ADD_4(new String[]{"PPOA4"}),
    PPO_ADD_5(new String[]{"PPOA5"}),
    PPO_ADD_6(new String[]{"PPOA6"}),
    PPO_CONTACT(new String[]{"PPOC"}),
    PPO_EMAIL(new String[]{"PPOEM"}),
    PPO_FAX(new String[]{"PPOF"}),
    PPO_PHONE(new String[]{"PPOP"}),
    PPO_TAX_ID(new String[]{"PPOTN"}),
    PPO_VENDOR_NAME(new String[]{"PPO"}),
    SEX(new String[]{"SEX"}),
    SSN(new String[]{"SSN", "SSN2"}),
    TERM_DATE(new String[]{"TRM1"}),
    TODAY_DATE(new String[]{"ISS"}),
    VIS_COVERAGE(new String[]{"VTOC"}),
    NOT_FOUND(new String[]{"N/A"});
    
	/**
	 * Array of codes that belongs to the corresponding constant.
	 */
    private final String[] codeList;
    
    /**
     * Assign the array of codes to the constant.
     * 
     * @param codeList
     * 		Array of variable identifiers.
     */
    IDCardHeader(String[] codeList) {
        this.codeList = codeList;
    }
    
    /**
     * Get the array of codes that represent the constant.
     * 
     * @return
     * 		The array of codes.
     */
    public String[] getCodeList() {
        return codeList;
    }
    
    /**
     * Get the header constant based on the given code.
     * 
     * @param code
     * 		Code that represents the constant.
     * @return
     * 		<b>IDCardHeader</b> Corresponding header.
     */
    public static IDCardHeader getHeader(String code) {
        String[] hdCodeList;
        
        for (IDCardHeader header : IDCardHeader.values()) {
            hdCodeList = header.getCodeList();
            
            if (Arrays.asList(hdCodeList).contains(code)) {
                return header;
            }
        }
        
        return NOT_FOUND;
    }
}
