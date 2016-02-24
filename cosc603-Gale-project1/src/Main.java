
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FireDanger fireIndex= new FireDanger( 1,2,false,4,5,6,7);
		
		fireIndex.calcFireDanger();
		
		System.out.println("The Dry Factor is: " + fireIndex.GetDF());
		System.out.println("The Fine Fuel Moisture is: " + fireIndex.GetFFM());
		System.out.println("The Adjusted Fuel Moisture is: " + fireIndex.GetADFM());
		System.out.println("The Grass Spread Index is: " + fireIndex.GetGrass());
		System.out.println("The Timber Spread Index is: " + fireIndex.GetTimber());
		System.out.println("The Build Up Index is: " + fireIndex.GetBUI());
		System.out.println("The Fire Load Rating is: " + fireIndex.GetFLoad());

	}

}
