import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;


public class WeatherObservation implements Comparable<WeatherObservation>{
	private int year;
	private int month;
	private double tmax=1000;
	private double tmin=-1000;
	private boolean provisional;
	private int af=-1000;
	private double rain=-1000;
	private double sun=-1000;
	
	public WeatherObservation(int ayear,int amonth){
		year=ayear;
		month=amonth;
	}
	
	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public double getTmax() {
		return tmax;
	}
	public void setTmax(double tmax) {
		if((tmax<-100||tmax>70)&&((tmax+1000.0)>1))
			System.out.println(tmax+"please input valid data.");
		else{	
		this.tmax = tmax;
		}
		
	}
	public double getTmin() {
		return tmin;
	}
	public void setTmin(double tmin) {
		if((tmin<-100||tmin>70)&&((tmax+1000.0)>1))
			System.out.println(tmin+"please input valid data.");
		else{	
		this.tmin = tmin;
		}
		
	}
	
	public boolean isProvisional() {
		return provisional;
	}
	public void setProvisional(boolean provisional) {
		this.provisional = provisional;
	}
	public double getAf()throws UndefinedException {
//		if(af==-1000)
//			throw new UndefinedException();
		return af;
	}
	public void setAf(int af) {
		this.af = af;
	}
	public double getRain() throws UndefinedException {
		if(rain==-1000)
			throw new UndefinedException();
		return rain;
	}
	public void setRain(double rain) {
		this.rain = rain;
	}
	public double getSun() throws UndefinedException {
//		if(sun==-1000)
//			throw new UndefinedException();
		return sun;
	}
	public void setSun(double sun) {
		this.sun = sun;
	}
	public double getMeanTemp() throws UndefinedException{
		if(tmin==-1000||tmax==-1000)
			throw new UndefinedException();
		return 0.5*(tmin+tmax);
		
	}
	public double getCompletion(){//ÌîÁË¼¸¸ö¿Õ£¿
		int hasdata=1;
		if(tmax!=-1000)
			hasdata++;
		if(tmin!=-1000)
			hasdata++;
		if(af!=-1000)
			hasdata++;
		if(rain!=-1000)
			hasdata++;
		if(sun!=-1000)
			hasdata++;
		return hasdata/6.0;
	}
	
	public String asRecord(){
		String s="";
		s+=year+"\t"+month+"\t"+(tmax==-1000?"---":tmax)+"\t"+(tmax==-1000?"---":tmin)+"\t"+(af==-1000?"---":af)+"\t"+(rain==-1000?"---":rain)
				+"\t"+(sun==-1000?"---":sun);
		return s;
	}
	public static boolean isEmpty(String s){
		return s.equals("---");
		
	}
	public static double str2doub(String s){
		return Double.valueOf(s);
	}
	public static int str2int(String s){
		return Integer.valueOf(s);
	}
	public static void main(String[] args) throws FileNotFoundException {
		SortedSet<WeatherObservation> observations=new TreeSet<WeatherObservation>();
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
			first.tmax=-1000;
		}
		else {
			
			first.setTmax(str2doub(tmax));
		}

		if(isEmpty(tmin)){
			first.tmin=-1000;
		}
		else {			
			first.setTmin(str2doub(tmax));
		}
		if(isEmpty(af)){
			first.af=-1000;
		}
		else {			
			first.setAf((int)str2int(af));
		}

		if(isEmpty(rain)){
			first.rain=-1000;
		}
		else {			
			first.setRain(str2doub(rain));
		}

		if(isEmpty(sun)){
			first.sun=-1000;
		}
		else {			
			first.setSun(str2doub(sun));
		}
		observations.add(first);
		}
		Iterator<WeatherObservation> it=observations.iterator();
		while(it.hasNext()){
			WeatherObservation cur=(WeatherObservation)it.next();
			System.out.println(cur.asRecord());
		};
		
	}

	@Override
	public int compareTo(WeatherObservation o) {
		if(o.year-year==0)
			return (month-o.month);
		return (year-o.year);
	}
}

