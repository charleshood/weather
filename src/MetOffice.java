import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;


public class MetOffice {

	/**
	 * @param args
	 */
	private List<WeatherStation> weatherStations;
	public MetOffice(){
		weatherStations=new ArrayList<WeatherStation>();
	}

	public void addStationfromFile(String filename) throws FileNotFoundException{
		//can not handle Provisional yet
		Scanner in=new Scanner(new File(filename));
		String name=in.nextLine();
		String otherinfo=in.nextLine();
		String[] locandsea=otherinfo.split(",");
		String local=locandsea[0].substring(locandsea[0].indexOf(":")+2);
		System.out.println(local);
		LatLong pos=new LatLong(local);
		String seal=locandsea[1].substring(1,locandsea[1].indexOf("met")-1);
		int sealevel=Integer.valueOf(seal);
		if(!locandsea[1].contains("amsl")){
			sealevel=-sealevel;			
		}
		WeatherStation london=new WeatherStation(name,pos,sealevel);
		for(int i=0;i<5;i++)
			in.nextLine();
		while(in.hasNextLine()){
			
			
/*			int year=(int) in.nextInt();
			int month=(int) in.nextInt();
			String tmax=in.next();
			String tmin=in.next();
			String af=in.next();
			String rain=in.next();
			String sun=in.next();*/
			String dataLine=in.nextLine();
			String dataL[]=dataLine.split("  ");
			String ss[]=new String[8];
			int count=0;
			for(int i=0;i<dataL.length;i++){
				if(dataL[i].trim().length()!=0)
					ss[count++]=dataL[i].trim();
			}
			int year=Integer.valueOf(ss[0]);
			int month=Integer.valueOf(ss[1]);
			String tmax=ss[2];
			String tmin=ss[3];
			String af=ss[4];
			String rain=ss[5];
			String sun=ss[6];
			boolean pro=false;
			if(ss.length==8)
				pro=true;			
			WeatherObservation first=new WeatherObservation(year,month);
			if(WeatherStation.isEmpty(tmax)){
				first.setTmax(-1000);
			}
			else {
				first.setTmax(WeatherStation.str2doub(tmax));
			}
	
			if(WeatherStation.isEmpty(tmin)){
				first.setTmin(-1000);
			}
			else {			
				first.setTmin(WeatherStation.str2doub(tmax));
			}
			if(WeatherStation.isEmpty(af)){
				first.setAf(-1000);
			}
			else {			
				first.setAf(WeatherStation.str2int(af));
			}
	
			if(WeatherStation.isEmpty(rain)){
				first.setRain(-1000);
			}
			else {			
				first.setRain(WeatherStation.str2doub(rain));
			}
	
			if(WeatherStation.isEmpty(sun)){
				first.setSun(-1000);
			}
			else {

				first.setSun(WeatherStation.str2doub(sun));
			}
			first.setProvisional(pro);
			try {
				london.addObservation(first);
			} catch (DataduplicationException e) {
				e.printStackTrace();
			}
		}
		weatherStations.add(london);
		
	}
	
	public void addWeatherStation(WeatherStation s){
		weatherStations.add(s);
		
	}
	public List<WeatherStation> findStation(String keywords){
		List<WeatherStation> results=new ArrayList<WeatherStation>();
		Iterator<WeatherStation> it=weatherStations.iterator();
		while(it.hasNext()){
			WeatherStation cur=it.next();
			if(cur.getName().contains(keywords)){
				results.add(cur);
			}			
		}
		return results;
		
	}
	public WeatherStation findNearestStation(LatLong here){
		double nearestdistance=1000000;
		WeatherStation nearStation=null;
		Iterator<WeatherStation> it=weatherStations.iterator();
		while(it.hasNext()){
			WeatherStation cur=it.next();
			if(cur.getPosition().getDistance(here)<nearestdistance){
				nearestdistance=cur.getPosition().getDistance(here);
				nearStation=cur;
			}
		}
		return nearStation;
		
	}
	/**
	 * Returns an Image object that can then be painted on the screen. 
	 * The url argument must specify an absolute {@link URL}. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method always returns immediately, whether or not the 
	 * image exists. When this applet attempts to draw the image on
	 * the screen, the data will be loaded. The graphics primitives 
	 * that draw the image will incrementally paint on the screen. 
	 *
	 * @param  begin  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the image at the specified URL
	 * @see         Image
	 */
	public int findYearLargestRainFall(int begin,int end) throws NotExistRainException, UndefinedException{
		double largestRainFall=0;
		double curRainFall=0;
		int largestRainFallYear=0;
		for(int i=begin;i<=end;i++){
			Iterator<WeatherStation> it=weatherStations.iterator();
			while(it.hasNext()){
				WeatherStation cur=it.next();
				curRainFall+=cur.getTotalRain(i);
			}
			if(curRainFall>largestRainFall){
				largestRainFall=curRainFall;
				largestRainFallYear=i;
			}
		}
		return largestRainFallYear;
			
		
	}
	public double MeanMonthlyTemperature(double latitude) throws UndefinedException{
		Iterator<WeatherStation> it=weatherStations.iterator();
		double MMT=0.0;
		int count=0;
		while(it.hasNext()){
			WeatherStation cur=it.next();
			if(cur.getPosition().getLatitude()>latitude){
				MMT+=cur.getMeanMonthlyTemprature();
				count++;
			}
		}
		if(count==0){
			System.out.println("north of a given latitude has no station");
		}
		return  MMT/count;
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, UndefinedException, NotExistRainException {
		//找到站名
		//从站名读数据
		MetOffice office=new MetOffice();
		office.intial();
		System.out.println("please input the station name:");
		Scanner in=new Scanner(System.in);
		String keywords=in.nextLine();		
		List<WeatherStation> list=office.findStation(keywords);
		System.out.println("the result has "+list.size()+" stations:");
		for(int i=0;i<list.size();i++)
			System.out.print(list.get(i).getName()+",");
		System.out.println("please input the station name:");
		keywords=in.nextLine();	
		list=office.findStation(keywords);
		WeatherStation result=list.get(0);
		office.addWeatherStation(result);
		Iterator<WeatherObservation> it=result.getObservations().iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			System.out.println(cur.asRecord());

		};
//		System.out.println("hear");		
//		System.out.println(result.getObservation(2005, 1).asRecord());
//		System.out.println(result.getWetYear());
//		System.out.println(result.getWetYearSecondMethod(1911,1984));
//		System.out.println("sunyear");
//		System.out.println(result.info());
//		System.out.println(result.getSunMonth());
		

	}

	private void intial() throws FileNotFoundException {
		// TODO Auto-generated method stub
		addStationfromFile("data/a.txt");
		addStationfromFile("data/b.txt");
		addStationfromFile("data/c.txt");
		addStationfromFile("data/d.txt");
		System.out.println(weatherStations.size()+" station has been inserted.");

	}

}
