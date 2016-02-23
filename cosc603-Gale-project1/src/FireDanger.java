
public class FireDanger {
	
	public FireDanger(double dry,    double wet,  double iSnow, 
					  double precip, double wind, double buo,   double iHerb){
		
		dry = dry;
		
	}
	public static void calcFireDanger(){
		if (!snow){
			CalcFineFuelMoisture( dry, wet);
			CalcDrayingFactor();
			AdjustFineFuelForHerbStage();
			if(rain){
				AdjustBUI();
			}
			IncBUIbyDryingFactor();
			CalcFuelAdjustedFuelMoist();
			if(fineFuel > 33){
				ChangeSpread();
			}
			else{
				if(windGreater > 14){
					caculateGrassTimberSpread();
				}
				else{
					caculateGrassTimberSpread();
				}
				if(BUI != 0 || Timber !=0){
					CalcFireLoadIndex();
				}
			}
		}
		else{
			ChangeSpread();
			if (rain){
				AdjustBUI();
			}
		}
	}
	
	private static double CalcFineFuelMoisture(double x, double y){
		
	}
	private static void CalcDrayingFactor(){
		
	}
	private static void AdjustFineFuelForHerbStage(){
		
	}
	private static void IncBUIbyDryingFactor(){
		
	}
	private static void CalcFuelAdjustedFuelMoist(){
		
	}
	private static void caculateGrassTimberSpread(){
		
	}
	private static void CalcFireLoadIndex(){
		
	}
	private static void ChangeSpread(){
		
	}
	private static void AdjustBUI(){
		
	}
	
	private static double dry;
	private static double wet;
	private static double iSnow;
	private static double precip;
	private static double wind;
	private static double buo;
	private static double iHerb;
	
	private static double df;
	private static double ffm;
	private static double adfm;
	private static double grass;
	private static double timber;
	private static double fload;
	
			
			
}
