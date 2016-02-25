
// TODO: Auto-generated Javadoc
/**
 * The Class FireDanger.
 */
public class FireDanger {
	
	/**
	 * Instantiates a new fire danger.
	 *
	 * @param dry Bulb temperature
	 * @param wet Bulb temperature
	 * @param iSnow is a some positive non zero number if there is snow on the ground
	 * @param precip is a number if there has been precipitation
	 * @param wind is the wind speed
	 * @param buo is the last value of Build up index
	 * @param iHerb the the current herb state of the district 1 = Cured, 2 = Transitions, 3 = Green
	 */
	public FireDanger(double dry,    double wet,  double iSnow, 
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
	
	
	/**
	 * Calc fire danger.
	 */
	public void calcFireDanger(){
		
		double tempPrecip = mPrecip - .1;
		
		if (miSnow <= 0){
			
			CalcFineFuelMoisture();
			CalcDrayingFactor();
			
			if (mFfm < 1){
				mFfm = 1; 
			}
				
			AdjustFineFuelForHerbStage();
			
			if( tempPrecip > 0){
				AdjustBUI();
			}
			
			IncBUIbyDryingFactor();
			CalcFuelAdjustedFuelMoist();
			
			double factorA = 0;
			double factorB = 0;
			
			if( mAdfm > 30 && mFfm > 30){
				//set Grass and timber to 1
				ChangeSpread( 1 );
				}
			else{
				if(mWind > 14){
					factorA = 0.009184;
					factorB = 14.4;
				}				
				else{
					factorA = 0.01312;
					factorB = 6.0;
				}
				if ( mFfm < 30){
					ChangeSpread(1);
				}
				else{
					calculateTimberSpread(factorA, factorB);
				}
				calculateGrassSpread(factorA, factorB);
				
				if(mBUI > 0 && mTimber > 0){
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
	
	public double GetFLoad(){
		return mFload;
	}
	
	public double GetDF(){
		return mDf;
	}
	
	public double GetFFM(){
		return mFfm;
	}
	
	public double GetADFM(){
		return mAdfm;
	}
	
	public double GetGrass(){
		return mGrass;
	}
	
	public double GetTimber(){
		return mTimber;
	}
	
	public double GetBUI(){
		return mBUI;
	}
	
	/**
	 * Calc fine fuel moisture.
	 */
	private static void CalcFineFuelMoisture(){
		
		double dif = mDry - mWet;
		
		int i = 0;
		for (i = 0; i < mC.length - 1; i++){
			if (dif < mC[i]){
				break;
			}
		}
		
		mFfm = mB[i] * Math.exp(mA[i]);
		
	}
	
	/**
	 * Calc draying factor.
	 */
	private static void CalcDrayingFactor(){
		
		int i = 0;
		for(i = 0; i < mD.length; i++){
			if ( mFfm - mD[i] > 0 ){
				break;
			}
		}
		mDf = i;
		
	}
	
	/**
	 * Adjust fine fuel for herb stage.
	 */
	private static void AdjustFineFuelForHerbStage(){
		
		mFfm = mFfm + ( miHerb - 1 ) * 5;
		
	}
	
	/**
	 * Inc bu iby drying factor.
	 */
	private static void IncBUIbyDryingFactor(){
		mBUI = mBUI + mDf;
		
	}
	
	/**
	 * Calc fuel adjusted fuel moist.
	 */
	private static void CalcFuelAdjustedFuelMoist(){
		
		mAdfm = .9 * mFfm + .5 + 9.5 * Math.exp(-mBUI/50);
		
	}
	
	/**
	 * Calculate grass timber spread.
	 *
	 * @param doGrass the do grass
	 * @param doTimber the do timber
	 * @param a the a
	 * @param b the b
	 */
	private static void calculateGrassSpread( double a, double b){
		double grassIndex = 0; 
		grassIndex = a * (mWind + b) * Math.pow((33 - mFfm ), 1.65) - 3;
		if ( grassIndex < 1){
			grassIndex = 1;
		}
		if ( grassIndex > 99){
			grassIndex = 99;
		}
		mGrass = grassIndex;
	}
	private static void calculateTimberSpread( double a, double b){
		double timberIndex = 0;
		timberIndex = a * (mWind + b) * Math.pow((33 - mAdfm ), 1.65) - 3;
		if ( timberIndex < 1){
			timberIndex = 1;
		}
		if ( timberIndex > 99){
			timberIndex = 99;
		}
		mTimber = timberIndex;
	}
	
	/**
	 * Calc fire load index.
	 */
	private static void CalcFireLoadIndex(){
		mFload = 1.75 * Math.exp(mTimber) + .32 * Math.exp(mBUI) -1.640;
		if (mFload < 0){
			mFload = 0;
		}
		else{
			mFload = Math.pow(10, mFload);
		}
	}
	
	/**
	 * Change spread.
	 *
	 * @param index the index
	 */
	private static void ChangeSpread(double index){
		mGrass = index;
		mTimber = index;
	}
	
	/**
	 * Adjust BUI.
	 */
	private static void AdjustBUI(){
		mBUI = -50 * ( Math.log(1 - ( Math.exp( -mBUO / 50))) * Math.exp(1.175 * (mPrecip - .1)));
		if (mBUI < 0 ){
			mBUI = 0;
		}
		
	}
	
	/** The Dry bulb temperature. */
	private static double mDry;
	
	/** The wet bulb temperature. */
	private static double mWet;
	
	/** positie none zero number if there is snow on the ground. */
	private static double miSnow;
	
	/** The m precip. */
	private static double mPrecip;
	
	/** The m wind. */
	private static double mWind;
	
	/** The m buo. */
	private static double mBUO;
	
	/** The mi herb. */
	private static int miHerb;
	
	/** The m df. */
	private static double mDf;
	
	/** The m ffm. */
	private static double mFfm;
	
	/** The m adfm. */
	private static double mAdfm;
	
	/** The m grass. */
	private static double mGrass;
	
	/** The m timber. */
	private static double mTimber;
	
	/** The m fload. */
	private static double mFload;
	
	/** The m bui. */
	private static double mBUI;
	
	/** The m a. */
	private static double[] mA = new double[4];
	
	/** The m b. */
	private static double[] mB = new double[4];
	
	/** The m c. */
	private static double[] mC = new double[3];
	
	/** The m d. */
	private static double[] mD = new double[6];
	
			
			
}
