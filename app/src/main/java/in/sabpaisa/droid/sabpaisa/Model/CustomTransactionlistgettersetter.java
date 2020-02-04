package in.sabpaisa.droid.sabpaisa.Model;

import java.util.List;

public class CustomTransactionlistgettersetter {
    private String clientName;
    private  String nooftransactions;
    private List<CustomTransactionReportgettersetter> CustomTransactionReportgettersetter;

    public List<CustomTransactionReportgettersetter> getCustomTransactionReportgettersetter() {
        return CustomTransactionReportgettersetter;
    }

    public void setCustomTransactionReportgettersetter(List<CustomTransactionReportgettersetter> customTransactionReportgettersetter) {
        CustomTransactionReportgettersetter = customTransactionReportgettersetter;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getNooftransactions() {
        return nooftransactions;
    }

    public void setNooftransactions(String nooftransactions) {
        this.nooftransactions = nooftransactions;
    }
}
