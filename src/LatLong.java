import static java.lang.Math.*;
public class LatLong{
	final double r=6371.393;
	private double latitude;
	private double longitude;
	public LatLong(double lat,double lon){
		latitude=lat;
		longitude=lon;
	}
	public LatLong(String latlong){//ÔõÃ´×ª»»
		String a[]=latlong.split(" ");
		switch (a[0].charAt(a[0].length()-1)) {
		case 'E':
			int min=Integer.parseInt(a[0].substring(a[0].length()-3,a[0].length()-1));
			int degree=Integer.parseInt(a[0].substring(0, a[0].length()-3));
			longitude=degree+min/60.0;
			break;
		case 'W':
			int wmin=Integer.parseInt(a[0].substring(a[0].length()-3,a[0].length()-1));
			int wdegree=Integer.parseInt(a[0].substring(0, a[0].length()-3));
			longitude=-wdegree-wmin/60.0;
			break;
		default:
			break;
		}
		switch (a[1].charAt(a[1].length()-1)) {
		case 'N':
			int min=Integer.parseInt(a[1].substring(a[1].length()-3,a[1].length()-1));
			int degree=Integer.parseInt(a[1].substring(0, a[1].length()-3));
			latitude=degree+min/60.0;
			break;
		case 'S':
			int wmin=Integer.parseInt(a[1].substring(a[1].length()-3,a[1].length()-1));
			int wdegree=Integer.parseInt(a[1].substring(0, a[1].length()-3));
			latitude=-wdegree-wmin/60.0;
			break;
		default:
			break;
		}
		
	}
	public double getLatitude(){
		return latitude;
	}
	public static double degree2Radian(double degree){
		return degree/180*3.1415;
	}
	public double getLongitude(){
		return longitude;
	}
	public double getDistance(LatLong a){
		
		double inside=sin(degree2Radian(a.latitude))*sin(degree2Radian(latitude))+cos(degree2Radian(a.latitude))*cos(degree2Radian(latitude))*cos(degree2Radian(a.longitude-longitude));
		double d=r*acos(inside);
		return d;
	}
	public String latLong2string() {
		int latdegree,latmin,londegree,lonmin;
		latdegree=(int)latitude;
		latmin=(int) ((latitude-latdegree)*60);
		londegree=(int)longitude;
		lonmin=(int) ((longitude-londegree)*60);
		String latlong=abs(londegree)+""+abs(lonmin)+""+(londegree>=0?"E":"W");
		latlong=latlong+" "+abs(latdegree)+""+abs(latmin)+""+(latdegree>=0?"N":"S");
		return latlong;
	}


	public static void main(String[] args){
		LatLong a=new LatLong("3234E 6026N");
		System.out.println(a.getLatitude());
		System.out.println(a.latLong2string());
		LatLong b=new LatLong("11341E 3011N");
		System.out.println(b.getLatitude());
		System.out.println(b.latLong2string());
		System.out.println(a.getDistance(b));
	}

}


