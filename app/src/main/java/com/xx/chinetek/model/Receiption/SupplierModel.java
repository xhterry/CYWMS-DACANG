package com.xx.chinetek.model.Receiption;

/**
 * Created by GHOST on 2017/5/2.
 */

public class SupplierModel{

    private String SupplierID;
    private String SupplierName;
    private String VoucherNo;
    private String ERPVoucherNo;
    private String VoucherType;
    private String StrVoucherType;
    private String Company;
    private String Department;

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getSupplierID() {
            return SupplierID;
        }

        public void setSupplierID(String supplierID) {
            SupplierID = supplierID;
        }

        public String getSupplierName() {
            return SupplierName;
        }

        public void setSupplierName(String supplierName) {
            SupplierName = supplierName;
        }

        public String getVoucherNo() {
            return VoucherNo;
        }

        public void setVoucherNo(String voucherNo) {
            VoucherNo = voucherNo;
        }

        public String getERPVoucherNo() {
            return ERPVoucherNo;
        }

        public void setERPVoucherNo(String ERPVoucherNo) {
            this.ERPVoucherNo = ERPVoucherNo;
        }

        public String getVoucherType() {
            return VoucherType;
        }

        public void setVoucherType(String voucherType) {
            VoucherType = voucherType;
        }

        public String getStrVoucherType() {
            return StrVoucherType;
        }

        public void setStrVoucherType(String strVoucherType) {
            StrVoucherType = strVoucherType;
        }
}
