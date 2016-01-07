import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream.GetField;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.spec.IvParameterSpec;


public class WeatherStation {
	private String name;
	private LatLong position;
	private int sealevel;
	private int opening;
	private int closing;
	private SortedSet<WeatherObservation> observations;
	private int starttime;
	private int endtime;
	public WeatherStation(String aname,LatLong pos,int seal){
		name=aname;
		position=pos;
		sealevel=seal;
		observations=new TreeSet<WeatherObservation>();
	}
	public WeatherStation(String data){
		String info[]=data.split(",");
		name=info[0];
		String locandsea[]=info[1].split(" ");
		String po=locandsea[1]+" "+locandsea[2];
		LatLong pos=new LatLong(po);
		position=pos;
		String seal=locandsea[3].substring(0, locandsea[3].length()-1);
		sealevel=Integer.valueOf(seal);
		if(!locandsea[4].equals("amsl"))
			sealevel=-sealevel;		
	}
	public SortedSet<WeatherObservation> getObservations(){
		return observations;
	}
	public String getName(){
		return name;
	}
	
	public LatLong getPosition(){
		return position;
	}
	
	
	
	public void addObservation(WeatherObservation b)throws DataduplicationException{//抛出已存在异常
		if(observations.contains(b)){
			throw new DataduplicationException();
		}
		else{
			observations.add(b);
		}
	}
	public WeatherObservation getObservation(int year,int month){
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getYear()<year){
				continue;
			}
			if(cur.getMonth()<month){
				continue;
			}
			return cur;
		}
		return null;
	}
	public double getTotalRain(int year)throws NotExistRainException, UndefinedException{//抛出不存在异常
		double totalRain=0.0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getYear()<year){
				continue;
			}
			if(cur.getYear()>year){
				return totalRain;
			}
			if(cur.getRain()==-1000){
				throw new NotExistRainException();
			}
			else{
				totalRain+=cur.getRain();
			}
		};
		
		return totalRain;
		
        }
	public double getMeanMonthlyTemprature() throws UndefinedException{
		double meanMonthTemprature=0;
		int count=0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			meanMonthTemprature+=cur.getMeanTemp();
			count++;
		}
		return meanMonthTemprature/count;
			
	}
	
	public double getAllRain()throws NotExistRainException, UndefinedException{//抛出不存在异常
		double totalRain=0.0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getRain()==-1000){
				throw new NotExistRainException();
			}
			else{
				totalRain+=cur.getRain();
			}
		}
		
		return totalRain;
		
     }
	
	public double getAllSun()throws NotExistRainException, UndefinedException{//抛出不存在异常
		double totalsun=0.0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getRain()==-1000){
				throw new NotExistRainException();
			}
			else{
				totalsun+=cur.getSun();
			}
		}
		
		return totalsun;
		
     }
	
	public double getAllaf()throws NotExistRainException, UndefinedException{//抛出不存在异常
		double totalaf=0.0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getRain()==-1000){
				throw new NotExistRainException();
			}
			else{
				totalaf+=cur.getAf();
			}
		}
		
		return totalaf;
		
     }
	
	
	
	
	public int getWetYear() throws UndefinedException{
		int maxyear=0;
		int curyear=0;
		double maxrain=0;
		double currain=0;
		Iterator<WeatherObservation> it=observations.iterator();
		WeatherObservation first=it.next();
		curyear=first.getYear();
		currain+=first.getRain();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getYear()==curyear){
				currain+=cur.getRain();				
			}
			else{
				if(currain>maxrain){
					maxyear=curyear;
					maxrain=currain;
				}
				currain=0;
				curyear=cur.getYear();
				currain+=cur.getRain();
			}
	    }
		return maxyear;

	}
	public int getWetYearSecondMethod(int begin,int end) throws UndefinedException, NotExistRainException{
		int maxyear=0;
		double maxrain=0;
		for(int i=begin;i<end;i++){
			if(getTotalRain(i)>maxrain){
				maxyear=i;
				maxrain=getTotalRain(i);
			}
		}
		return maxyear;

	}
	

	public String getSunMonth() throws UndefinedException{
		WeatherObservation sunyear=null;
		double maxsun=0;
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			if(cur.getSun()>maxsun){
				sunyear=cur;
				maxsun=cur.getSun();
			}
		}
		return sunyear.getYear()+"."+sunyear.getMonth();
		
	}

	public double getCompletion(){//平均值，缺失用0
		double comp=0;
		Iterator<WeatherObservation> it=observations.iterator();
		WeatherObservation cur=(WeatherObservation)it.next();
		int startyear=cur.getYear();
		int startmonth=cur.getMonth();
		int endyear=0;
		int endmonth=0;
		comp+=cur.getCompletion();
		while(it.hasNext()){
			cur=(WeatherObservation)it.next();
			comp+=cur.getCompletion();
			endyear=cur.getYear();
			endmonth=cur.getMonth();		
		}
		int recordCount=endyear-startyear-1;
		recordCount=recordCount+(13-startmonth)+endmonth;
		return comp/recordCount;
		
	}

	public String info() throws NotExistRainException, UndefinedException{//name,positon,totalrain,totalsun,totalaf
	  String info="";
	  info+=name+"\t"+"longtitude:"+position.getLongitude()+"\t"+"latitude:"+position.getLatitude()+"\t";
	  info=info+"totalRainFall: "+getAllRain()+"\t"+"totalSunHours: "+getAllSun()+"\t"+"total Number of days of air frost:"+getAllaf();
	  return info;
	}

	public WeatherStation getStationfromFile(String filename) throws FileNotFoundException{
		LatLong londonpos=new LatLong("3234E 6026N");
		WeatherStation london=new WeatherStation("London", londonpos,(int)300);
		WeatherStation esk=new WeatherStation("Eskdalemuir"+"\n"+"Location 3234E 6026N 242m amsl");
		System.out.println(esk.name);
		System.out.println(esk.sealevel);
		System.out.println(esk.position.getLongitude());
		System.out.println(esk.position.getLatitude());
		Scanner in=new Scanner(new File(filename));
		while(in.hasNextLine()){
			int year=(int) in.nextInt();
			int month=(int) in.nextInt();
			String tmax=in.next();
			String tmin=in.next();
			String af=in.next();
			String rain=in.next();
			String sun=in.next();
			WeatherObservation first=new WeatherObservation(year,month);
			if(isEmpty(tmax)){
				first.setTmax(-1000);
			}
			else {
				
				first.setTmax(str2doub(tmax));
			}
	
			if(isEmpty(tmin)){
				first.setTmin(-1000);
			}
			else {			
				first.setTmin(str2doub(tmax));
			}
			if(isEmpty(af)){
				first.setAf(-1000);
			}
			else {			
				first.setAf(str2int(af));
			}
	
			if(isEmpty(rain)){
				first.setRain(-1000);
			}
			else {			
				first.setRain(str2doub(rain));
			}
	
			if(isEmpty(sun)){
				first.setSun(-1000);
			}
			else {
				if(sun.charAt(sun.length()-1)=='#'||sun.charAt(sun.length()-1)=='*'){
					sun=sun.substring(0, sun.length()-1);
				}
				first.setSun(str2doub(sun));
			}
			try {
				london.addObservation(first);
			} catch (DataduplicationException e) {
				e.printStackTrace();
			}
		}
		return london;
		
	}
	public static boolean isEmpty(String s){
		return s.equals("---");
		
	}
	public static double str2doub(String s){
		if(s.contains("*")||s.contains("#")){
			s=s.substring(0, s.length()-1);			
		}
		return Double.valueOf(s);
	}
	public static int str2int(String s){
		if(s.contains("*")||s.contains("#")){
			s=s.substring(0, s.length()-1);			
		}
		return Integer.valueOf(s);
	}
	
	public static void main(String[] args) throws FileNotFoundException, UndefinedException, NotExistRainException {
		LatLong londonpos=new LatLong("3234E 6026N");
		WeatherStation london=new WeatherStation("London", londonpos,(int)300);
		WeatherStation esk=new WeatherStation("Eskdalemuir"+"\n"+"Location 3234E 6026N 242m amsl");
		System.out.println(esk.name);
		System.out.println(esk.sealevel);
		System.out.println(esk.position.getLongitude());
		System.out.println(esk.position.getLatitude());
		Scanner in=new Scanner(new File("a.txt"));
		while(in.hasNextLine()){
		int year=(int) in.nextInt();
		int month=(int) in.nextInt();
		String tmax=in.next();
		String tmin=in.next();
		String af=in.next();
		String rain=in.next();
		String sun=in.next();
		WeatherObservation first=new WeatherObservation(year,month);
		if(isEmpty(tmax)){
			first.setTmax(-1000);
		}
		else {
			
			first.setTmax(str2doub(tmax));
		}

		if(isEmpty(tmin)){
			first.setTmin(-1000);
		}
		else {			
			first.setTmin(str2doub(tmax));
		}
		if(isEmpty(af)){
			first.setAf(-1000);
		}
		else {			
			first.setAf(str2int(af));
		}

		if(isEmpty(rain)){
			first.setRain(-1000);
		}
		else {			
			first.setRain(str2doub(rain));
		}

		if(isEmpty(sun)){
			first.setSun(-1000);
		}
		else {
			if(sun.charAt(sun.length()-1)=='#'){
				sun=sun.substring(0, sun.length()-1);
			}
			first.setSun(str2doub(sun));
		}
		try {
			london.addObservation(first);
		} catch (DataduplicationException e) {
			e.printStackTrace();
		}
		}
		Iterator<WeatherObservation> it=london.observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			System.out.println(cur.getRain());

		};
		System.out.println(london.getObservation(2005, 1).asRecord());
		System.out.println(london.getWetYear());
		System.out.println(london.getWetYearSecondMethod(1911,1984));
		System.out.println("sunyear");
		System.out.println(london.info());
		System.out.println(london.getSunMonth());
		try {
			System.out.println(london.getTotalRain((int)1984));
		} catch (NotExistRainException e) {
			System.out.println("sorry, the data is missing");
			e.printStackTrace();
		}
	}
		

	} 


