package Calculator.dto;

/**
 * ImpressionInput.
 */
public class ImpressionInput {
    public ImpressionInput(String name, int impressionPerCampaign, int revenuePerCampaign){
        Name = name;
        ImpressionPerCampaign = impressionPerCampaign;
        RevenuePerCampaign = revenuePerCampaign;
    }

    /**
     * Name of the customer.
     */
    public String Name;

    /**
     * Amount of impressions per campaign.
     */
    public int ImpressionPerCampaign;

    /**
     * Revenue per campaign.
     */
    public int RevenuePerCampaign;
}
