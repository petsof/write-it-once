package org.simart.writeonce.domain;

@Describe
@Builder
public class Dtest {
    private String dtestfield;

    public String getDtestfield() {
        return dtestfield;
    }

    public void setDtestfield(String dtestfield) {
        this.dtestfield = dtestfield;
    }
}
