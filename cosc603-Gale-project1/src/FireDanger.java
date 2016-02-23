
public class FireDanger {

	public static void calcFireDanger(){
		if (!snow){
			CalcFineFuelMoisture();
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
					caculateGrassTimberSpreadOne();
				}
				else{
					caculateGrassTimberSpreadTwo();
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
	
	private static void CalcFineFuelMoisture(){
		
	}
	private static void CalcDrayingFactor(){
		
	}
	private static void AdjustFineFuelForHerbStage(){
		
	}
	private static void IncBUIbyDryingFactor(){
		
	}
	private static void CalcFuelAdjustedFuelMoist(){
		
	}
	private static void caculateGrassTimberSpreadOne(){
		
	}
	private static void caculateGrassTimberSpreadTwo(){
		
	}
	private static void CalcFireLoadIndex(){
		
	}
	private static void ChangeSpread(){
		
	}
	private static void AdjustBUI(){
		
	}
}
