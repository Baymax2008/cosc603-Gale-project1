
// TODO: Auto-generated Javadoc
/**
 * The Class FireDanger.
 */
public class FireDanger {
	
	/**
	 * Instantiates a new fire danger.
	 *
	 * @param dry is the Dry Bulb Temperature
	 * @param wet is the Wet Bulb Temperature
	 * @param iSnow is some positive non zero number if there is snow on the ground
	 * @param precip the amount precipitation in inches and hundredths
	 * @param wind the wind speed mph 20 feet above open level ground
	 * @param buo was yesterday's buildup
	 * @param iHerb is the current herb state of the district 1=cured, 2=Transition and 3=Green
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
	 * Calculate fire danger.
	 */
	public void calcFireDanger(){
		
		double tempPrecip = mPrecip - .1;
		
		// If there is no snow on the ground
		if (miSnow <= 0){
			
			CalcFineFuelMoisture();
			CalcDryingFactor();
			
			if (mFfm < 1){
				mFfm = 1; 
			}
				
			AdjustFineFuelForHerbStage();
			
			if( tempPrecip > 0){
				// Precipitation Exceeded, reduce the BUI
				AdjustBUI();
			}
			
			IncBUIbyDryingFactor();
			CalcFuelAdjustedFuelMoist();
			
			double factorA = 0;
			double factorB = 0;
			
			if( mAdfm > 30 && mFfm > 30){
				// Set Timber and Grass Index to 0
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
					// Set Timber and Grass Index to 1
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
		else{ // There is snow on the ground
			
			// Set Timber and Grass Index to 0
			ChangeSpread( 0 );
			
			if (tempPrecip > 0){
				AdjustBUI();
			}
		}
	}
	
	/**
	 * Gets the Fire Load Rating.
	 *
	 * @return the double
	 */
	public double GetFLoad(){
		return mFload;
	}
	
	/**
	 * Gets the Dry Factor.
	 *
	 * @return the double
	 */
	public double GetDF(){
		return mDf;
	}
	
	/**
	 * Gets the Fine Fuel Moisture.
	 *
	 * @return the double
	 */
	public double GetFFM(){
		return mFfm;
	}
	
	/**
	 * Gets the Adjusted (10 day lag) fuel moisture.
	 *
	 * @return the double
	 */
	public double GetADFM(){
		return mAdfm;
	}
	
	/**
	 * Gets the Grass spread index.
	 *
	 * @return the double
	 */
	public double GetGrass(){
		return mGrass;
	}
	
	/**
	 * Gets the Timber spread index.
	 *
	 * @return the double
	 */
	public double GetTimber(){
		return mTimber;
	}
	
	/**
	 * Gets the Build Up Index.
	 *
	 * @return the double
	 */
	public double GetBUI(){
		return mBUI;
	}
	
	/**
	 * Calculate Fine fuel Moisture.
	 */
	private static void CalcFineFuelMoisture(){
		
		double dif = mDry - mWet;
		
		int i = 0;
		
		// Find the coefficients need to calculate the Fine Fuel Moisture
		// Once the difference is less to the c value the program will break the loop
		for (i = 0; i < mC.length - 1; i++){
			if (dif < mC[i]){
				break;
			}
		}
		
		// i should not exceed 4
		mFfm = mB[i] * Math.exp(mA[i]);
		
	}
	
	/**
	 * Calculate Drying Factor.
	 */
	private static void CalcDryingFactor(){
		
		int i = 0;
		
		// Find the coefficients need to calculate the Drying Factory
		for(i = 0; i < mD.length; i++){
			if ( mFfm - mD[i] > 0 ){
				break;
			}
		}
		
		// Okay to if I gets to 7
		mDf = i;
		
	}
	
	/**
	 * Adjust fine fuel for herb stage.
	 */
	private static void AdjustFineFuelForHerbStage(){
		
		mFfm = mFfm + ( miHerb - 1 ) * 5;
		
	}
	
	/**
	 * Inc BUI by drying factor.
	 */
	private static void IncBUIbyDryingFactor(){
		mBUI = mBUI + mDf;
		
	}
	
	/**
	 * Calculate fuel adjusted fuel moist.
	 */
	private static void CalcFuelAdjustedFuelMoist(){
		
		mAdfm = .9 * mFfm + .5 + 9.5 * Math.exp(-mBUI/50);
		
	}
	
	/**
	 * Calculate Grass Spread Index.
	 *
	 * @param a the a factor based on wind speed
	 * @param b the b factor based on wind speed
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
	
	/**
	 * Calculate Timber Spread Index.
	 *
	 * @param a the a factor based on wind speed
	 * @param b the b factor based on wind speed
	 */
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
	 * Calculate Fire Load Rating.
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
	 * Change Grass and Timber spread index.
	 *
	 * @param index is the Index to change Grass and Timber
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
	
	/** the Dry Bulb Temperature. */
	private static double mDry;
	
	/** the Wet Bulb Temperature. */
	private static double mWet;
	
	/** if there is snow */
	private static double miSnow;
	
	/** the precipitation. */
	private static double mPrecip;
	
	/** The wind speed. */
	private static double mWind;
	
	/** Yesterday's build up index. */
	private static double mBUO;
	
	/** The herb factor. */
	private static int miHerb;
	
	/** The drying factor. */
	private static double mDf;
	
	/** The Fine fuel moisture. */
	private static double mFfm;
	
	/** The adjusted fuel moisture. */
	private static double mAdfm;
	
	/** The Grass Spread Index. */
	private static double mGrass;
	
	/** The Timber spread Index. */
	private static double mTimber;
	
	/** The Fire Load Index. */
	private static double mFload;
	
	/** The Build Up Index. */
	private static double mBUI;
	
	/** coefficients  needed in calculating FFM */
	private static double[] mA = new double[4];
	
	/** coefficients needed in calculating FFM */
	private static double[] mB = new double[4];
	
	/** differences needed in determine what numbers to use in  FFM */
	private static double[] mC = new double[3];
	
	/** coefficients used to calculate drying factor of the day. */
	private static double[] mD = new double[6];
	
			
			
}
