
public class FireDanger {
	
	public FireDanger(double dry,    double wet,  boolean iSnow, 
					  double precip, double wind, double buo,   int iHerb){
		
		mDry = dry;
		mWet = wet;
		miSnow = iSnow;
		mPrecip = precip;
		mWind = wind;
		mBUO = buo;
		miHerb = iHerb;
		
		mDf = 0;
		mFfm = 0;
		mAdfm = 0;
		mGrass = 0;
		mTimber = 0;
		mFload = 0;
		mBUI = 0;
		
		mA[0] = -0.1859;
		mA[1] = -0.859;
		mA[2] = -0.05966;
		mA[3] = -0.077373;
		
		mB[0] = 30;
		mB[1] = 19.2; 
		mB[2] = 13.8; 
		mB[3] = 22.5;
				
		mC[0] = 4.5;
		mC[1] = 12.5;
		mC[2] = 27.5;
	
		mD[0] = 16;
		mD[1] = 10;
		mD[2] = 7;
		mD[3] = 5;
		mD[4] = 4;
		mD[5] = 3;
		
	}
	
	
	public static void calcFireDanger(){
		
		double tempPrecip = mPrecip - .1;
		
		if (!miSnow){
			CalcFineFuelMoisture();
			CalcDrayingFactor();
			AdjustFineFuelForHerbStage();
			
			if( tempPrecip > 0){
				AdjustBUI();
			}
			IncBUIbyDryingFactor();
			CalcFuelAdjustedFuelMoist();
			if( mAdfm > 33 ){
				 if( mFfm > 33){
					 //set Grass and timber to 1
					 ChangeSpread( 1 );
				 }
				
			}
			else{
				if(mWind > 14){
					caculateGrassTimberSpread();
				}
				else{
					caculateGrassTimberSpread();
				}
				if(mBUO != 0 || mTimber !=0){
					CalcFireLoadIndex();
				}
			}
		}
		else{
			ChangeSpread( 0 );
			if (tempPrecip > 0){
				AdjustBUI();
			}
		}
	}
	
	private static void CalcFineFuelMoisture(){
		
		double dif = mDry - mWet;
		
		int i = 0;
		for (i = 0; i < mC.length; i++){
			if (dif < mC[i]){
				break;
			}
		}
		
		mFfm = mB[i] * Math.exp(mA[i]);
		
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
	private static void ChangeSpread(double index){
		mGrass = index;
		mTimber = index;
	}
	private static void AdjustBUI(){
		mBUI = -50 * ( Math.log(1 - ( Math.exp( -mBUO / 50))) * Math.exp(1.175 * (mPrecip - .1)));
		if (mBUI < 0 ){
			mBUI = 0;
		}
		
	}
	
	private static double mDry;
	private static double mWet;
	private static boolean miSnow;
	private static double mPrecip;
	private static double mWind;
	private static double mBUO;
	private static int miHerb;
	
	private static double mDf;
	private static double mFfm;
	private static double mAdfm;
	private static double mGrass;
	private static double mTimber;
	private static double mFload;
	private static double mBUI;
	private static double[] mA = new double[4];
	private static double[] mB = new double[4];
	private static double[] mC = new double[3];
	private static double[] mD = new double[6];
	
			
			
}
