import java.util.Random;

public class LeverageSimulation {
  public static void main(String args[]) {
    
    // parameters: play with them
    int leverage = 2;
    int tradingDays = 252;
    int investmentDurationYears = 30;
    double indexRoiPercent = 7.0;
    double averageDailyFluctuationPercent = 0.75;
    double terBase = 0.07;
    double terLeverage = 0.6;
    
    // loop over all years
    double currentTotalRoi = 1.0;
    double currentTotalRoiLeverage = 1.0;
    for (int j = 0; j < investmentDurationYears; j++) {
        
        double dailyRoiMean = Math.pow((1 + indexRoiPercent/100), 1.0/tradingDays);
        
        double[] allReturnsBase = new double[tradingDays];
        double[] allReturnsLeverage = new double[tradingDays];
        
        // loop over all days in a year and find daily returns
        Random random = new Random();
        for (int i = 0; i < tradingDays; i++) {
            double randomValue = random.nextGaussian();
            
            // find daily returns for the non-leverage etf and the leveraged version: costs included and substracted daily
            allReturnsBase[i] = dailyRoiMean + averageDailyFluctuationPercent * randomValue/100;
            allReturnsLeverage[i] = allReturnsBase[i] * leverage - (leverage-1) - terLeverage/(100*tradingDays);
            allReturnsBase[i] = allReturnsBase[i] - terBase/(100*tradingDays);
        }
        // turn daily returns into yearly
        double yearlyRoiBase = 1.0;
        double yearlyRoiLeverage = 1.0;
        for (int i = 0; i < tradingDays; i++) {
            yearlyRoiBase = yearlyRoiBase * allReturnsBase[i];
            yearlyRoiLeverage = yearlyRoiLeverage * allReturnsLeverage[i];
        }
        
        // turn yearly returns into overall performance
        currentTotalRoi = currentTotalRoi * yearlyRoiBase;
        currentTotalRoiLeverage = currentTotalRoiLeverage * yearlyRoiLeverage;
    }
    
    double yearlyPercentBase = (Math.pow(currentTotalRoi, 1.0/investmentDurationYears) -1) * 100;
    double yearlyPercentLeverage = (Math.pow(currentTotalRoiLeverage, 1.0/investmentDurationYears) -1) * 100;
    
    yearlyPercentBase = Math.round(yearlyPercentBase * 100.0) / 100.0;
    yearlyPercentLeverage = Math.round(yearlyPercentLeverage * 100.0) / 100.0;
    System.out.println("Yearly Returns Base: " + yearlyPercentBase + "%");
    System.out.println("Yearly Returns Lev: " + yearlyPercentLeverage + "%");
    System.out.println("");
    currentTotalRoi = Math.round(currentTotalRoi * 100.0) / 100.0;
    currentTotalRoiLeverage = Math.round(currentTotalRoiLeverage * 100.0) / 100.0;
    System.out.println("1€ invested would turn into " + currentTotalRoi + "€ (Base) after " + investmentDurationYears + " years");
    System.out.println("1€ invested would turn into " + currentTotalRoiLeverage + "€ (Leverage) after " + investmentDurationYears + " years");
  }
}