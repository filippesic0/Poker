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
public class Igrac
{	
	public int novac;
	public static int potpreflop,potflop,potturn;
	public String ime;
	public Karta karta1,karta2;
	public Turnir turnir;
	public int referentnavrednost;
    public boolean fold=false;
	public Podaci podaci;
	
	//MASINSKO UCENJE:

	//Pošto je plan sa neuronskim mrežama propao, atributi su diskretizovani.
	public Istorija istorija;
	public int pocetninovac,dosadasnjiulog;
	public int novacflopturnriver[]=new int[3];;
	public static final int brojakcija[]={13,22,22,22};//za 1 manje
	public ArrayList<Integer>[] akcije=new ArrayList[4];
	public static final double[] fibonacipreflop=
		{0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, Double.POSITIVE_INFINITY};
	public static final double[] fibonaciflopturnriver=
		{0, 1/144.0, 1/89.0, 1/55.0, 1/34.0, 1/21.0, 1/13.0, 1/8.0, 1/5.0, 1/3.0, 1/2.0, 1/1.5, 
		1, 1.5, 2, 3, 5, 8, 13, 21, 34, Double.POSITIVE_INFINITY};
	public static double alfastart,alfa=0.001,gama=1;
	
	public Igrac(int a,String b,Turnir t,Podaci p)
    {   novac=a;
		turnir=t;
        //sprintf_s(ime,"%s",b);
		ime=b;
		karta1=new Karta();
		karta2=new Karta();
		istorija=new Istorija();
		referentnavrednost=t.buyin/100;
		for(int i=0;i<4;i++)
			akcije[i]=new ArrayList<>();
		podaci=p;
    }
	
	public Igrac(int a,String b,int buyin,Podaci p)
	{	novac=a;
		//sprintf_s(ime,"%s",b);
		ime=b;
		karta1=new Karta();
		karta2=new Karta();
		turnir=new Turnir();
		referentnavrednost=buyin;
		istorija=new Istorija();
		for(int i=0;i<4;i++)
			akcije[i]=new ArrayList<>();
		podaci=p;
	}
	
	public Igrac()
	{	novac=0;
		ime="";
		karta1=new Karta();
		karta2=new Karta();
		istorija=new Istorija();
		for(int i=0;i<4;i++)
			akcije[i]=new ArrayList<>();
	}
	
	public void deal(Karta k1, Karta k2)
    {   karta1=k1;
        karta2=k2;
		pocetninovac=novac;
	/*	verovatnoca[0]=-1;
		verovatnoca[1]=-1;
		verovatnoca[2]=-1;*/
		for(int i=0;i<3;i++)
			novacflopturnriver[i]=-1;
    }
	
	public void blind(int a)
	{	if(a<=novac)
		{	novac-=a;
			dosadasnjiulog+=a;
		}
		else
		{	dosadasnjiulog+=novac;
			novac=0;
		}
	}
	
	public void fold()
	{	ucenjepreflopa();
		ucenjeflopturnriver();
		fold=true;
	}
	
	public int call(int a)
	{	if(a<=novac)
		{	novac-=a;
			return a;
		}
		else
		{	novac=0;
			return a;
		}
	}
	
	public void allin()
	{	dosadasnjiulog+=novac;
		novac=0;
	}
	
	public void winner(int n,int pracenje,int type) throws IOException
    {   if(pracenje==2)
			System.in.read();
			//getchar();
        novac+=n;
		if(novac>50000)
			novac+=0;
		if(pracenje>0)
		{	if(n>0&&type==0)
				System.out.printf("\n%s %s %s nosi %d$.\n",ime,karta1,karta2,n);
				//wcout<<'\n'<<ime<<' '<<karta1<<' '<<karta2<<" nosi "<<n<<"$.";
			else if(n>0)
				System.out.printf("\n%s nosi %d$.\n",ime,n);
				//wcout<<'\n'<<ime<<" nosi "<<n<<"$.";
			if(pracenje==2)
				System.in.read();
		}
    }
	
	@Override
	public String toString()
	{	return String.format("%-10s %7d$",ime,novac);
	}
	
    public boolean equals(Igrac igrac)
	{	if(this.ime.equals(igrac.ime))
			if(this.karta1==igrac.karta1)
				if(this.karta2==igrac.karta2)
					return true;
		return false;
	}

    public int preflop(int ulog,int najvisenovca, int brojigraca,int brojbetova)
	{	istorija.parametripreflop(this,brojbetova);
		int h,karte,b,n,n1,po;
		b=Math.min(istorija.brojbetova[3],2);
		n=istorija.brojigraca[3];
		n1=istorija.brojigraca1[3];
		po=istorija.pozicija[3];
		karte=Karta.dvekarte(karta1,karta2);
		double politika1[]=podaci.Q[karte][b][n][n1][po],politika[]=new double[13];
		//Nećemo da gledamo samo za konkretnu akciju,
		//već ćemo delom gledati i prethodnu i sledeću akciju da bismo brže i preciznije učili:
		politika[0]=politika1[0]*2/3+politika1[1]/3;
		politika[brojakcija[0]-1]=politika1[brojakcija[0]-1]*2/3+politika1[brojakcija[0]-2]/3;
		for(h=1;h<brojakcija[0]-1;h++)
			politika[h]=politika1[h-1]/2+politika1[h]+politika1[h+1]/2;
		return preflopflopturnriver(0,ulog,0,najvisenovca,politika,politika1);
	}
    
	public int flopturnriver(int flop,List<Igrac> igraci,int ulog,int ukupanulog,int najvisenovca,int brojigraca,int brojbetova,Karta f1,Karta f2,Karta f3, Karta f4, Karta f5) throws IOException
	{   Karta karte[];
		istorija.parametriflopturnriver(this,flop,brojbetova,ukupanulog);
		if(najvisenovca==0)
			return 0;
		switch(flop)
		{	case 0:
				karte=new Karta[]{karta1, karta2, f1, f2, f3};
			break;
			case 1:
				karte=new Karta[]{karta1, karta2, f1, f2, f3, f4};
			break;
			default:
				karte=new Karta[]{karta1, karta2, f1, f2, f3, f4, f5};
		}
		if(istorija.verovatnoca[flop]<0)
		{	/*karta1=new Karta(2,5);
			karta2=new Karta(4,5);
			f1=new Karta(2,6);
			f2=new Karta(1,8);
			f3=new Karta(1,9);
			f4=new Karta(1,10);
			f5=new Karta(1,2);
			karte=new Karta[]{karta1, karta2, f1, f2, f3, f4, f5};*/
			istorija.verovatnoca[flop]=Verovatnoca.Pflopturnriver(igraci,this, brojigraca, karte,flop+3);
		}
		int h,v,b,n,n1,po,np;
		b=Math.min(istorija.brojbetova[flop],2);
		n=istorija.brojigraca[flop];
		n1=istorija.brojigraca1[flop];
		po=istorija.pozicija[flop];
		np=istorija.novacpot[flop];
		v=Math.round(istorija.verovatnoca[flop]*100);
		double politika1[]=podaci.Qflopturnriver[flop][v][b][n][n1][po][np];
		double politika[]=new double[22];
		//Nećemo da gledamo samo za konkretnu akciju,
		//već ćemo delom gledati i prethodnu i sledeću akciju da bismo brže i preciznije učili:
		if(v>0)
		{	double prethodnapolitika[]=podaci.Qflopturnriver[flop][v-1][b][n][n1][po][np];
				for(h=0;h<brojakcija[1];h++)
					politika[h]+=prethodnapolitika[h]/2;
		}
		if(v<100)
		{	double sledecapolitika[]=podaci.Qflopturnriver[flop][v+1][b][n][n1][po][np];
				for(h=0;h<brojakcija[1];h++)
					politika[h]+=sledecapolitika[h]/2;
		}
		politika[0]=politika1[0]*2/3+politika1[1]/3;
		politika[20]=politika1[20]*2/3+politika1[19]/3;
		for(h=1;h<brojakcija[1]-1;h++)
			politika[h]+=politika1[h-1]/2+politika1[h]+politika1[h+1]/2;
		return preflopflopturnriver(flop+1,ulog,ukupanulog,najvisenovca,politika,politika1);
	}
	
	public int preflopflopturnriver(int flop,int ulog,int ukupanulog,int najvisenovca,double politika[],double politika1[])
	{	//postavlja se za h=0, tj. fold:
		double fibonaci[];
		if(flop==0)
			fibonaci=fibonacipreflop;
		else
			fibonaci=fibonaciflopturnriver;
		
		int h,hmin=1,akcija;
		ArrayList<Integer> indmaxes=new ArrayList<>();
		ArrayList<Integer> indexes=new ArrayList<>();
		double max=(float)(novac-pocetninovac)/referentnavrednost;
	//	System.out.printf("%.0f ",max);
		for(h=0;h<brojakcija[flop];h++)
		{	if(flop==0)
			{	if(h>0&&Ruka.bigblind*fibonaci[h-1]>novac+dosadasnjiulog)
					break;
				if(Ruka.bigblind*fibonaci[h]<(float)ulog)
				{	hmin=h+1;
					continue;
				}
				if(ulog>0&&h>hmin&&Ruka.bigblind*fibonaci[h]<2*(float)ulog)//po pravilima ne moze da se digne za manje od toga
					continue;
			}
			else
			{	if(h>0&&ukupanulog*fibonaci[h-1]>novac+dosadasnjiulog)
					break;
				if(ukupanulog*fibonaci[h]<(float)ulog)
				{	hmin=h+1;
					continue;
				}
				else if(h>hmin&&ukupanulog*fibonaci[h]<Ruka.bigblind)//po pravilima ne moze da se digne za manje od toga
					continue;
				if(ulog>0&&h>hmin&&ukupanulog*fibonaci[h]<2*(float)ulog)//po pravilima ne moze da se digne za manje od toga
					continue;
			}
			if(Math.abs(politika[h])==0&&Math.abs(politika1[h])==0)
			{	max=0;
				indmaxes=new ArrayList<>();
				indmaxes.add(h);
				break;
			}
			double nagrada=politika[h];
			/*if(flop>0)
				try{
					nagrada=Neuronska_mreza.mreza.output(istorija1, h-flop*10, 2);
				}catch (Exception e){}*/
			if(Math.abs(nagrada-max)<0.1)
			{	if(nagrada>max)
				{	max=nagrada;
				indmaxes.clear();
				indmaxes.add(h);}
			}
			else if(nagrada>max+0.1)
			{	max=nagrada;
				indmaxes=new ArrayList<>();
				indmaxes.add(h);
			}
			indexes.add(h);
	//		System.out.printf("%.0f ",politika[h]*1000);
		}
	//	System.out.println();
		if(indmaxes.isEmpty())
			if(ulog>0)
				return -1;
			else
				return 0;
		Random rand = new Random();
		float random=rand.nextFloat();
		if(max==0.0)
			random=1;
		if(random<(flop+1)/20.0*0)//biramo random akciju
			akcija=indexes.get(rand.nextInt(indexes.size()));
		else
			akcija=indmaxes.get(rand.nextInt(indmaxes.size()));

		if(akcija>=0)
			akcije[flop].add(akcija);
		if(flop<3)
			novacflopturnriver[flop]=novac-ulog;
		switch (akcija)
		{	case 0:
				return ulog;
			default:
				int akcija1;
				if(flop==0)
					akcija1=(int)(Ruka.bigblind*fibonaci[akcija]);
				else
					akcija1=(int)(ukupanulog*fibonaci[akcija]);
				if(akcija==hmin)
					akcija1=ulog;
				if(akcija1>novac+dosadasnjiulog)
					akcija1=novac+dosadasnjiulog;
				if(novac>=Ruka.bigblind&&akcija1<Ruka.bigblind&&akcija1>0)
					akcija1=Ruka.bigblind;
				if(akcija1>najvisenovca)
					akcija1=najvisenovca;
				return akcija1;
		}
	}
    
	//MASINSKO UCENJE:

	public void ucenjepreflopa()
	{	if (podaci.id=="tridana")
				return;
		
		double nagrada=novac-pocetninovac;
		if(turnir.buyin!=0)
			nagrada*=100.0/turnir.buyin/Turnir.bigblind*turnir.pocetnibigblind;
		else
			nagrada/=referentnavrednost;
		if(nagrada==0)
			return;
		nagrada*=gama;
		
		int karte,b,n,n1,po;
		b=istorija.brojbetova[3];
		n=istorija.brojigraca[3];
		n1=istorija.brojigraca1[3];
		po=istorija.pozicija[3];
		karte=Karta.dvekarte(karta1,karta2);
		double politika[]=podaci.Q[karte][b][n][n1][po];
		int brojac[]=podaci.Qbrojac[karte][b][n][n1][po];
		for(int akcija:akcije[0])
		{	alfa=1.0/(++brojac[akcija]);
			if(alfa<0.01)
				alfa=0.01;
			politika[akcija]+=alfa*(nagrada-politika[akcija]);
			brojac[akcija]++;
		}
	}
	
	public void ucenjeflopturnriver()
	{	if (podaci.id=="tridana")
				return;
		
		for(int flop=0;flop<3;flop++)
			if(istorija.verovatnoca[flop]>-1)
			{	double nagrada;
				if(akcije[flop+1].contains((Integer)(-1)))
				{	System.out.print("Istorija linija 374");
					System.exit(0);
					nagrada=0;
				}	
				nagrada=novac-pocetninovac/*-novacflopturnriver[flop]*/;
				if(turnir.buyin!=0)
					nagrada*=100.0/turnir.buyin/Turnir.bigblind*turnir.pocetnibigblind;
				else
					nagrada/=referentnavrednost;
				nagrada*=gama;
				if(nagrada==0)
					continue;

				if(nagrada>100||nagrada<-100)
				{	System.out.print("Istorija linija 388");
					System.exit(0);
					nagrada=0;
				}	
				
				int v,b,n,n1,po,np;
				b=istorija.brojbetova[flop];
				n=istorija.brojigraca[flop];
				n1=istorija.brojigraca1[flop];
				po=istorija.pozicija[flop];
				np=istorija.novacpot[flop];
				v=Math.round(istorija.verovatnoca[flop]*100);
				double politika[]=podaci.Qflopturnriver[flop][v][b][n][n1][po][np];
				for(int akcija:akcije[flop+1])
					politika[akcija]+=alfa*(nagrada-politika[akcija]);
			}
	}
}