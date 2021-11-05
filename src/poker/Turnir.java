package poker;

import java.io.IOException;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fpesic
 */
public class Turnir
{	
	public int brojigraca;
	public int buyin;
	public static int bigblind;
	public int pocetnibigblind;
	
	public Turnir()
	{	brojigraca=0;
		buyin=0;
		pocetnibigblind=0;
	}
	
	public Turnir(int a,int b,int c)
	{	brojigraca=a;
		buyin=b;
		pocetnibigblind=20;
	}
	
	public void igraj(int pracenje) throws IOException
	{	int smallblind=pocetnibigblind/2,brojacblindova=0,dugme=-1;
		bigblind=pocetnibigblind;
		List<Igrac> igraci=new ArrayList<>();
		System.out.printf("%d igraca, pocetni kapital %d$\n",brojigraca,buyin);
		//wcout<<brojigraca<<" igraca, pocetni kapital "<<ulog<<"$"<<endl;
		for(int i=0;i<brojigraca;i++)
		{	/*string ime("igrac");
			ime=ime+to_string((long long)(i+1));*/
			/*char ime[20];
			sprintf_s(ime,"igrac %d",i+1);*/
			String ime="igrac "+(i+1);
			Igrac novi=new Igrac(buyin,ime,this);
			igraci.add(novi);
		}
		int rruka=1;
		while(igraci.size()>1)
		{   dugme++;
			System.out.printf("%\n\nRUKA %d\n",rruka);
			//wcout<<endl<<endl<<"RUKA "<<rruka<<endl;
			rruka++;
			for(Igrac igrac:igraci)
				System.out.println(igrac);
			if(pracenje==1)
				System.in.read();
				//getchar();
			else
				System.out.println();
			while(dugme>=(int)igraci.size())
				dugme-=(int)igraci.size();
			Ruka ruka=new Ruka(igraci,dugme,smallblind,bigblind,pracenje);
			igraci=ruka.igraj();
			for(int j=0;j<igraci.size();)
			{	Igrac igrac=igraci.get(0);
				if(igrac.novac<=0)
				{	brojigraca--;
					igrac=igraci.remove(0);
				}
				else
					j++;
			}
			brojacblindova++;
			if(brojacblindova==5)
			{   bigblind*=2;
				brojacblindova=0;
				if(bigblind>buyin/10)
					bigblind=buyin/10;
				smallblind=bigblind/2;
			}
		}
	}
}