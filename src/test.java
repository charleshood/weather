
public class test {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="   2015   1    5.0    -0.6      14   354.9    37.0#  Provisional";
		String a[]=s.split("   ");
		System.out.println(a[1]);
		System.out.println(a[2]);
		System.out.println(a[3]);
		System.out.println(a[4]);
		System.out.println(a[5]);
		System.out.println(a[6]);
		String[] locandsea=new String[20];
		locandsea[0]="Location: 4267E 5415N";
		locandsea[1]=" 10 metres d metres";
		String local=locandsea[0].substring(locandsea[0].indexOf(":")+2);
        System.out.println(local);
        String seal=locandsea[1].substring(1,locandsea[1].indexOf("met")-1);
		int sealevel=Integer.valueOf(seal);
		if(!locandsea[1].contains("amsl")){
			sealevel=-sealevel;			
		}	
		System.out.println(sealevel);
		System.out.println("cur");
		String dataLine="  2015   4    8.3     3.4       2    67.3   132.4#  Provisional";
		String dataL[]=dataLine.split("  ");
		String ss[]=new String[8];
		int count=0;
		for(int i=0;i<dataL.length;i++){
			if(dataL[i].trim().length()!=0)
				ss[count++]=dataL[i].trim();
		}
		for(int i=0;i<ss.length;i++)
			System.out.println(ss[i]);
	}

}
