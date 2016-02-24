
// TODO: Auto-generated Javadoc
/**
 * The Class FireDanger.
 */
public class FireDanger {
	
	/**
	 * Instantiates a new fire danger.
	 *
	 * @param dry the dry
	 * @param wet the wet
	 * @param iSnow the isnow
	 * @param precip the precip
	 * @param wind the wind
	 * @param buo the buo
	 * @param iHerb the iherb
	 */
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
	
	
	/**
	 * Calc fire danger.
	 */
	public void calcFireDanger(){
		
		double tempPrecip = mPrecip - .1;
		
		if (!miSnow){
			CalcFineFuelMoisture();
			CalcDrayingFactor();
			
			/*
			 * Why?
			 */
			if (mFfm < 1){
				mFfm = 1; 
			}
				
			AdjustFineFuelForHerbStage();
			
			if( tempPrecip > 0){
				AdjustBUI();
			}
			IncBUIbyDryingFactor();
			CalcFuelAdjustedFuelMoist();
			if( mAdfm > 30 && mFfm > 30){
				//set Grass and timber to 1
				ChangeSpread( 1 );
				}
			else{
				
				boolean doTimber = true;
				
				if ( mFfm < 30){
					mTimber = 1;
					doTimber = false;	
				}
				if(mWind > 14){
					caculateGrassTimberSpread(true, doTimber, .009184, 14.4);
				}
				else{
					caculateGrassTimberSpread(true, doTimber, .01312, 6.0);
				}
				
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
	 * Caculate grass timber spread.
	 *
	 * @param doGrass the do grass
	 * @param doTimber the do timber
	 * @param a the a
	 * @param b the b
	 */
	private static void caculateGrassTimberSpread(boolean doGrass, boolean doTimber,
												  double a, double b){
		if (doGrass){
			mGrass = a * (mWind + b) * Math.pow((33 - mFfm ), 1.65) - 3;
			if ( mGrass < 1){
				mGrass = 1;
			}
			if ( mGrass > 99){
				mGrass = 99;
			}
		}
		if(doTimber){
			mTimber = a * (mWind + b) * Math.pow((33 - mAdfm ), 1.65) - 3;
			if ( mTimber < 1){
				mTimber = 1;
			}
			if ( mTimber > 99){
				mTimber = 99;
			}
		}
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
	 * Adjust bui.
	 */
	private static void AdjustBUI(){
		mBUI = -50 * ( Math.log(1 - ( Math.exp( -mBUO / 50))) * Math.exp(1.175 * (mPrecip - .1)));
		if (mBUI < 0 ){
			mBUI = 0;
		}
		
	}
	
	/** The m dry. */
	private static double mDry;
	
	/** The m wet. */
	private static double mWet;
	
	/** The mi snow. */
	private static boolean miSnow;
	
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
