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
	//char ime[50];
	public Karta karta1,karta2;
	public Turnir turnir;
	public int buyin;
	
	public Igrac(int a,String b,Turnir t)
	//Igrac(int a,char b[])
    {   novac=a;
		turnir=t;
		buyin=turnir.ulog;
        //sprintf_s(ime,"%s",b);
		ime=b;
		karta1=new Karta();
		karta2=new Karta();
    }
	
	public Igrac(int a,String b,int buy)
		//Igrac(int a,char b[])
	{	novac=a;
		//sprintf_s(ime,"%s",b);
		ime=b;
		buyin=buy;
		karta1=new Karta();
		karta2=new Karta();
		turnir=new Turnir();
	}
	
	public Igrac()
	{	novac=0;
		ime="";
		karta1=new Karta();
		karta2=new Karta();
	}
	
	public void deal(Karta k1, Karta k2)
    {   karta1=k1;
        karta2=k2;
		pocetninovac=novac;
		verovatnoca[0]=-1;
		verovatnoca[1]=-1;
		verovatnoca[2]=-1;
		novacflop=-1;
		novacturn=-1;
		novacriver=-1;
		/*akcijestart=new ArrayList<>();
		akcijeflop=new ArrayList<>();
		akcijeturn=new ArrayList<>();
		akcijeriver=new ArrayList<>();*/
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
	
	public void fold(int najvisenovca1)
	{	ucenjepreflopa();
		karta1=new Karta();
		karta2=new Karta();
		upisinagradu(najvisenovca1);
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
	
	int randomakcija(int ulog,int novac,int call,int minbet,int maxbet)
	{   int random;
		if(maxbet>9)
			maxbet=9;
		/*for(minbet=1;minbet<=9;minbet++)
			if((float)ulog/novac<=1.0/Igrac.fibonaci[minbet])
				break;*/
		if(maxbet<minbet)
			maxbet=minbet;
		if(call<minbet)
			maxbet++;
		Random rand = new Random();
		random=rand.nextInt(5);//Float.POSITIVE_INFINITY;//
		if(random==0)//fold/check
			return 0;
		random=(int)(rand.nextFloat()*0.999*(maxbet+1-minbet)+minbet);
		//random=(rand()%1000)*(maxbet+1-minbet)/1000+minbet;
		if(random<minbet)
			random=0;
		if(random==maxbet&&call<minbet)
			return call;
		return random;
	}

    public int preflop(int ulog,int najvisenovca, int brojigraca)
	{	Ulog[0]=0;
		Ulog[1]=0;
		Ulog[2]=0;
		ulog-=dosadasnjiulog;
		int k1=0,k2=0;
		if(karta1.id<=5)
			k1=0;
		else if(karta1.id<=7)
			k1=1;
		else if(karta1.id<=9)
			k1=2;
		else if(karta1.id<=10)
			k1=3;
		else if(karta1.id<=11)
			k1=4;
		else if(karta1.id<=12)
			k1=5;
		else if(karta1.id<=13)
			k1=6;
		else
			k1=7;
		if(karta2.id<=5)
			k2=0;
		else if(karta2.id<=7)
			k2=1;
		else if(karta2.id<=9)
			k2=2;
		else if(karta2.id<=10)
			k2=3;
		else if(karta2.id<=11)
			k2=4;
		else if(karta2.id<=12)
			k2=5;
		else if(karta2.id<=13)
			k2=6;
		else
			k1=7;
		if(k1<k2)
		{   int p=k2;
			k2=k1;
			k1=p;
		}
		double politika[]=new double[10];
		int politikabrojac[]=new int[10];
		if(karta1.boja!=karta2.boja&&karta1.id!=karta2.id)
		{	for(int i=0;i<10;i++)
			{	politika[i]=Q[k1][k2][i];
				politikabrojac[i]=Qbrojac[k1][k2][i];
			}
		}
		else if(karta1.id==karta2.id)
		{	for(int i=0;i<10;i++)
			{	politika[i]=Q[8][k2][i];
				politikabrojac[i]=Qbrojac[8][k2][i];
			}
		}
		else
		{	for(int i=0;i<9;i++)
			{	politika[i]=Qboja[k1][k2][i];
				politikabrojac[i]=Qbojabrojac[k1][k2][i];
			}
		}
		
		double max=Q[0][0][0];
		int indmax=0,h,hmin=1,hminbet=1,hmaxbet=1;
		for(h=1;h<10;h++)
		{	
			if(h>1&&(/*politikabrojac[h]<500||*/politika[h-1]<0))
				continue;
			hmaxbet=h;
			if((float)ulog/novac>1.0/fibonaci[h])
			{	hmin=h+1;
				hminbet=hmin;
				continue;
			}
			if(ulog>0&&h>hmin&&novac/fibonaci[h]<2*(float)ulog)//po pravilima ne moze da se digne za manje od toga
			{	hminbet=hmin;
				continue;
			}
			if(hminbet==hmin)
				hminbet=h;
			if(politika[h]>max)
			{	max=politika[h];
				indmax=h;
			}
			max=0;
		}
		if(hminbet<hmin)
			hminbet=hmin;
		if(hmaxbet>9)
			hmaxbet=9;
		if(hmin>9)
			hmin=9;
		if(hminbet>9)
			hminbet=9;
		Random rand = new Random();
		float random=rand.nextFloat();
		if((random<0.4||max==Double.NEGATIVE_INFINITY)&&hmin<hmaxbet)//biramo random akciju
			akcijastart=randomakcija(ulog,novac,hmin,hminbet,hmaxbet);
		else
			akcijastart=indmax;
		
		//IZBACITI SLEDECI DEO:
		/*indmax=1;
		random=10;*/
		//KRAJ IZBACIVANJA
		
		if(akcijastart==0)
		{   if(ulog<(float)novac/50)
			{   dosadasnjiulog+=ulog;
				novacflop=novac-ulog;
				akcijastart=1;
				return ulog;
			}
			else
				return -1;
		}
		if(akcijastart==1)
		{   if(novac<ulog)
				ulog=novac;
			dosadasnjiulog+=ulog;
			novacflop=novac-ulog;
			return ulog;
		}
		int akcija=novac/fibonaci[akcijastart];
		if(akcija>najvisenovca)
			akcija=najvisenovca;
		if(akcijastart==hmin)
			akcija=ulog;
		novacflop=novac-akcija;
		dosadasnjiulog+=akcija;
		return akcija;
	}
    
	public int flopturnriver(int flop,List<Igrac> igraci,int ulog,int najvisenovca,int najvisenovca1,int brojigraca,Karta f1,Karta f2,Karta f3, Karta f4, Karta f5) throws IOException
	{   if(najvisenovca==0)
			return 0;
		ulog-=dosadasnjiulog;
		Ulog[flop]=(double)ulog/najvisenovca;
		Novac[flop]=(double)novac/najvisenovca1;
		if(flop==0)
		{	int boja[]={0,0,0,0,0};
			boja[karta1.boja]++;
			boja[karta2.boja]++;
			boja[f1.boja]++;
			boja[f2.boja]++;
			boja[f3.boja]++;
			/*if(boja[1]<3&&boja[2]<3&&boja[3]<3&&boja[4]<3)
			{	int karte[]={0,0,0,0,0};
				if(karta1.id>=karta2.id)
				{	karte[0]=karta1.id;karte[1]=karta2.id;}
				else
				{	karte[1]=karta1.id;karte[0]=karta2.id;}
				karte[2]=f1.id;
				if(f2.id>f1.id)
				{	karte[2]=f2.id;karte[3]=f1.id;}
				else
					karte[3]=f2.id;
				if(f3.id<=karte[3])
					karte[4]=f3.id;
				else
				{	karte[4]=karte[3];
					if(f3.id<=karte[2])
						karte[3]=f3.id;
					else
					{	karte[3]=karte[2];karte[2]=f3.id;}
				}
				for(int i=0;i<5;i++)
					karte[i]-=2;
				verovatnoca[0]=Verovatnoca.verovatnoceflop.get(karte[0]).get(karte[1]).get(karte[2]).get(karte[3]).get(karte[4]);
			}
			else */
			Karta karte[]=new Karta[]{karta1, karta2, f1, f2, f3};
			if(verovatnoca[0]<0)
				verovatnoca[0]=Verovatnoca.Pflopturnriver(igraci,this, brojigraca, karte,3);
		}
		else if(flop==1)
		{	Karta karte[]=new Karta[]{karta1, karta2, f1, f2, f3, f4};
			if(verovatnoca[1]<0)
				verovatnoca[1]=Verovatnoca.Pflopturnriver(igraci,this, brojigraca, karte,4);
		}
 		else if(verovatnoca[2]<0)
		{	Karta karte[]=new Karta[]{karta1, karta2, f1, f2, f3, f4, f5};
			verovatnoca[2]=Verovatnoca.Pflopturnriver(igraci,this, brojigraca, karte,5);
		}
		
		int indmax=0,h,hmin=1,hminbet=1;
		double max=-dosadasnjiulog*100.0/buyin;
		for(h=1;h<10;h++)
		{	if((float)ulog/novac>1.0/fibonaci[h])
			{	hmin=h+1;
				hminbet=hmin;
				continue;
			}
			if(ulog>0&&h>hmin&&novac/fibonaci[h]<2*(float)ulog)//po pravilima ne moze da se digne za manje od toga
			{	hminbet=hmin;
				continue;
			}
			if(hminbet==hmin)
				hminbet=h;
			double nagrada=0;
			/*try{
				nagrada=Neuronska_mreza.mreza.output(istorija1, h-flop*10, 2);
			}catch (Exception e){}*/
			int u,n,v;
			u=(int)(Ulog[flop]*20);
			n=(int)(Novac[flop]*20);
			for(v=0;v<40;v++)
				if(podelaverovatnoca[v]>=verovatnoca[flop])
					break;
			v--;
			if(u==20)
				u--;
			if(n==20)
				n--;
			if(v<0)
				v=0;
			
			if(flop==0)
				nagrada=Qflop[v][u][n][h];
			else if(flop==1)
				nagrada=Qturn[v][u][n][h];
			else
				nagrada=Qriver[v][u][n][h];
			
			if(nagrada>max)
			{	max=nagrada;
				indmax=h;
			}
			if(h>1&&nagrada<1)
				break;
			u=0;
		}
		if(hminbet<hmin)
			hminbet=hmin;
		if(h>9)
			h=9;
		if(hmin>9)
			hmin=9;
		if(hminbet>9)
			hminbet=9;
		Random rand = new Random();
		float random=rand.nextFloat();
		if(random<0.2||max==Double.NEGATIVE_INFINITY)//biramo random akciju
			akcija[flop]=randomakcija(ulog,novac,hmin,hminbet,h);
		else
			akcija[flop]=indmax;
		
		//IZBACITI SLEDECI DEO:
		/*indmax=1;
		random=10;
		max=0;*/	
		//KRAJ IZBACIVANJA
		
		if(akcija[flop]==0)
		{   if(ulog<(float)novac/50)
			{   dosadasnjiulog+=ulog;
				if(flop==0)
					novacturn=novac-ulog;
				if(flop==1)
					novacriver=novac-ulog;
				akcija[flop]=1;
				return ulog;
			}
			else
				return -1;
		}
		if(akcija[flop]==1)
		{   if(novac<ulog)
				ulog=novac;
			dosadasnjiulog+=ulog;
			if(flop==0)
				novacturn=novac-ulog;
			if(flop==1)
				novacriver=novac-ulog;
			return ulog;
		}
		int akcija1=novac/fibonaci[akcija[flop]];
		if(akcija1>najvisenovca)
			akcija1=najvisenovca;
		if(akcija[flop]==hmin)
			akcija1=ulog;
		dosadasnjiulog+=akcija1;
		if(flop==0)
			novacturn=novac-akcija1;
		if(flop==1)
			novacriver=novac-akcija1;
		return akcija1;
	}
	
	{
	/*
	public int flop(int ulog,int najvisenovca,int najvisenovca1,int brojigraca,Karta f1,Karta f2,Karta f3)
	{	if(najvisenovca==0)
			return 0;
		ulog-=dosadasnjiulog;
		Ulogflop=(float)ulog/najvisenovca;
		Novacflop=(float)novac/najvisenovca1;
		if(istorija.size()==1)
			istorija.remove(0);
		Istorija istorija1=new Istorija();
		istorija1.brojigraca=brojigraca;
		istorija1.ulogflop=Ulogflop;
		istorija1.novacflop=Novacflop;
		istorija.add(istorija1);
		/*if(najvisenovca!=0)
			ulogflop+=(float)ulog/najvisenovca;*
		int boja[]={0,0,0,0,0};
		boja[karta1.boja]++;
		boja[karta2.boja]++;
		boja[f1.boja]++;
		boja[f2.boja]++;
		boja[f3.boja]++;
		//float verovatnocaflop1;
		if(boja[1]<3&&boja[2]<3&&boja[3]<3&&boja[4]<3)
		{	int karte[]={0,0,0,0,0};
			if(karta1.id>=karta2.id)
			{	karte[0]=karta1.id;karte[1]=karta2.id;}
			else
			{	karte[1]=karta1.id;karte[0]=karta2.id;}
			karte[2]=f1.id;
			if(f2.id>f1.id)
			{	karte[2]=f2.id;karte[3]=f1.id;}
			else
				karte[3]=f2.id;
			if(f3.id<=karte[3])
				karte[4]=f3.id;
			else
			{	karte[4]=karte[3];
				if(f3.id<=karte[2])
					karte[3]=f3.id;
				else
				{	karte[3]=karte[2];karte[2]=f3.id;}
			}
			for(int i=0;i<5;i++)
				karte[i]-=2;
			verovatnocaflop=Verovatnoca.verovatnoceflop.get(karte[0]).get(karte[1]).get(karte[2]).get(karte[3]).get(karte[4]);
		}
		if(verovatnocaflop<0)
			verovatnocaflop=Verovatnoca.Pflop(karta1, karta2, f1, f2, f3);
		istorija1.verflop=verovatnocaflop;
		/*if(verovatnocaflop>0.8)
		{	ulog=(int)max(round(novac/8.0),(double)ulog);
			istorija1.akcija=6;
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(verovatnocaflop>0.6&&ulog<novac/8.0)
		{	istorija1.akcija=ulog;
			if(ulog>0)
			{	double a=ulog/novac;
				for(int i=1;i<11;i++)
					if(a<novac/fibonaci[i])
						istorija1.akcija=i;
			}
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(ulog==0)
		{	istorija1.akcija=0;
			return 0;
		}
		else
		{	istorija1.akcija=-1;
			return -1;
		}*
		int indmax=1,h;
		double max=Double.NEGATIVE_INFINITY;
		for(h=1;h<10;h++)
		{	/*if(Ruka.nemoguci.get(h))
			{	//max=Double.NEGATIVE_INFINITY;
				break;
			}*
			if((float)ulog/novac>1.0/fibonaci[h])
				continue;
			double nagrada=0;
			int u=(int)(istorija1.ulogflop*20);
			int n=(int)(istorija1.novacflop*20);
			int v;
			for(v=0;v<40;v++)
				if(podelaverovatnoca[v]>=verovatnocaflop)
					break;
			v--;
			if(u==20)
				u--;
			if(n==20)
				n--;
			if(v<0)
				v=0;
			nagrada=Qflop[v][u][n][h];
			if(nagrada>max)
			{	max=nagrada;
				indmax=h; 
			}
			if(h>1&&(nagrada<0.1||Ruka.brojac.get(h)<=3000)||h>2)
				break;
			else
				max=max;
			
		}
		if(h<2)
			h=2;
		Random rand = new Random();
		float random=rand.nextFloat();
		
		//IZBACITI SLEDECI DEO:
		/*indmax=1;
		random=10;
		max=0;*
		//KRAJ IZBACIVANJA
		
		//float random=(float)(rand()%1000)/1000;
		if(random<1.5||max==Double.NEGATIVE_INFINITY)//biramo random akciju
			akcijaflop=randomakcija(ulog,novac,h);
		else
		{   if(indmax==1||1.0/fibonaci[indmax]>(float)ulog/novac&&(float)ulog/novac>1.0/fibonaci[indmax+1])//call
				akcijaflop=indmax;
			else//bet/raise
			{   if(ulog==0)
					akcijaflop=indmax;
				else if(1.0/fibonaci[indmax]>2*(float)ulog/novac)//po pravilima ne moze da se digne za manje od toga
					akcijaflop=indmax;
				else
					akcijaflop=1;
			}
		}
		akcijeflop.add(akcijaflop);
		istorija1.akcija=akcijaflop;
		int akcija;
		
		if(akcijaflop==0)
			akcija=0;
		else
			akcija=novac/fibonaci[akcijaflop];
		if(akcijaflop==0)
		{   if(ulog<(float)novac/50)
			{   dosadasnjiulog+=ulog;
				novacturn=novac-ulog;
				akcijaflop=1;
				istorija1.akcija=1;
				akcijeflop.set(akcijeflop.size()-1,1);
				return ulog;
			}
			else
				return -1;
		}
		if(akcijaflop==1||akcijaflop==10)
		{   if(novac<ulog)
				ulog=novac;
			dosadasnjiulog+=ulog;
			novacturn=novac-ulog;
			return ulog;
		}
		if(akcija>najvisenovca)
			akcija=najvisenovca;
		novacturn=novac-akcija;
		dosadasnjiulog+=akcija;
		return akcija;
	}
   
	public int turn(int ulog,int najvisenovca,int najvisenovca1,int brojigraca,Karta f1,Karta f2,Karta f3, Karta f4)
	{   if(najvisenovca==0)
			return 0;
		ulog-=dosadasnjiulog;
		Ulogturn=(float)ulog/najvisenovca;
		Novacturn=(float)novac/najvisenovca1;
		if(istorija.size()==2)
			istorija.remove(1);
		Istorija istorija1=new Istorija();
		istorija1.brojigraca=brojigraca;
		istorija1.ulogflop=Ulogflop;
		istorija1.novacflop=Novacflop;
		istorija1.ulogturn=Ulogturn;
		istorija1.novacturn=Novacturn;
		istorija.add(istorija1);
		/*if(najvisenovca!=0)
			ulogturn+=(float)ulog/najvisenovca;*
		if(verovatnocaturn<0)
			verovatnocaturn=Verovatnoca.Pturn(karta1, karta2, f1, f2, f3, f4);
		istorija1.verflop=verovatnocaflop;
		istorija1.verturn=verovatnocaturn;
		/*if(verovatnocaturn>0.85)
		{	ulog=(int)max(round(novac/5.0),(double)ulog);
			istorija1.akcija=7;
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(verovatnocaturn>0.6&&ulog<novac/6.0)
		{	istorija1.akcija=ulog;
			if(ulog>0)
			{	double a=ulog/novac;
				for(int i=1;i<11;i++)
					if(a<novac/fibonaci[i])
						istorija1.akcija=i;
			}
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(ulog==0)
		{	istorija1.akcija=0;
			return 0;
		}
		else
		{	istorija1.akcija=-1;
			return -1;
		}*
		int indmax=11,h;
		double max=Double.NEGATIVE_INFINITY;
		for(h=1;h<10;h++)
		{	/*if(Ruka.nemoguci.get(h+10))
			{	//max=Double.NEGATIVE_INFINITY;
				break;
			}*
			if((float)ulog/novac>1.0/fibonaci[h])
				continue;
			double nagrada=0;
			int u=(int)(istorija1.ulogturn*20);
			int n=(int)(istorija1.novacturn*20);
			int v;
			for(v=0;v<40;v++)
				if(podelaverovatnoca[v]>=verovatnocaturn)
					break;
			v--;
			if(u==20)
				u--;
			if(n==20)
				n--;
			if(v<0)
				v=0;
			nagrada=Qturn[v][u][n][h];
			if(nagrada>max)
			{	max=nagrada;
				indmax=h;
			}
			if(h>1&&(nagrada<0.1||Ruka.brojac.get(h+10)<=3000)||h>2)
				break;
			else
				max=max;
		}
		if(h<2)
			h=2;
		Random rand = new Random();
		float random=rand.nextFloat();
		
		//IZBACITI SLEDECI DEO:
		/*indmax=1;
		random=10;
		max=0;*
		//KRAJ IZBACIVANJA
		
		//float random=(float)(rand()%1000)/1000;
		if(random<1.5||max==Double.NEGATIVE_INFINITY)//biramo random akciju
			akcijaturn=randomakcija(ulog,novac,h);
		else
		{   if(indmax==1||1.0/fibonaci[indmax]>(float)ulog/novac&&(float)ulog/novac>1.0/fibonaci[indmax+1])//call
				akcijaturn=indmax;
			else//bet/raise
			{   if(ulog==0)
					akcijaturn=indmax;
				else if(1.0/fibonaci[indmax]>2*(float)ulog/novac)//po pravilima ne moze da se digne za manje od toga
					akcijaturn=indmax;
				else
					akcijaturn=1;
			}
		}
		akcijeturn.add(akcijaturn);
		istorija1.akcija=akcijaturn;
		int akcija;
		
		if(akcijaturn==0)
			akcija=0;
		else
			akcija=novac/fibonaci[akcijaturn];
		if(akcijaturn==0)
		{   if(ulog<(float)novac/50)
			{   dosadasnjiulog+=ulog;
				novacriver=novac-ulog;
				akcijaturn=1;
				istorija1.akcija=1;
				akcijeturn.set(akcijeturn.size()-1,1);
				return ulog;
			}
			else
				return -1;
		}
		if(akcijaturn==1||akcijaturn==10)
		{   if(novac<ulog)
				ulog=novac;
			dosadasnjiulog+=ulog;
			novacriver=novac-ulog;
			return ulog;
		}
		if(akcija>najvisenovca)
			akcija=najvisenovca;
		novacriver=novac-akcija;
		dosadasnjiulog+=akcija;
		return akcija;
		//return novac/fibonaci[akcijaturn];
	}
    
	public int river(int ulog,int najvisenovca,int najvisenovca1,int brojigraca,Karta f1,Karta f2,Karta f3, Karta f4, Karta f5)
	{   if(najvisenovca==0)
			return 0;
		ulog-=dosadasnjiulog;
		Istorija istorija1=new Istorija();
		Ulogriver=(float)ulog/najvisenovca;
		Novacriver=(float)novac/najvisenovca1;
		if(istorija.size()==3)
			istorija.remove(2);
		istorija1.brojigraca=brojigraca;
		istorija1.ulogflop=Ulogflop;
		istorija1.novacflop=Novacflop;
		istorija1.ulogturn=Ulogturn;
		istorija1.novacturn=Novacturn;
		istorija1.ulogriver=Ulogriver;
		istorija1.novacriver=Novacriver;
		istorija.add(istorija1);
		/*if(najvisenovca!=0)
			ulogriver+=(float)ulog/najvisenovca;*
		if(verovatnocariver<0)
			verovatnocariver=Verovatnoca.Priver(karta1, karta2, f1, f2, f3, f4, f5);
		istorija1.verflop=verovatnocaflop;
		istorija1.verturn=verovatnocaturn;
		istorija1.verriver=verovatnocariver;
		/*if(verovatnocariver>0.9)
		{	ulog=(int)max(round(novac/3.0),(double)ulog);
			istorija1.akcija=8;
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(verovatnocariver>0.6&&ulog<novac/4.0)
		{	istorija1.akcija=ulog;
			if(ulog>0)
			{	double a=ulog/novac;
				for(int i=1;i<11;i++)
					if(a<novac/fibonaci[i])
						istorija1.akcija=i;
			}
			dosadasnjiulog+=ulog;
			return ulog;
		}
		else if(ulog==0)
		{	istorija1.akcija=0;
			return 0;
		}
		else
		{	istorija1.akcija=-1;
			return -1;
		}*
		int indmax=21,h;
		double max=Double.NEGATIVE_INFINITY;
		for(h=1;h<10;h++)
		{	//if(Ruka.nemoguci.get(h+20))
			/*if(Ruka.istorija.size()<10001)
			{	//max=Double.NEGATIVE_INFINITY;
				break;
			}*
			if((float)ulog/novac>1.0/fibonaci[h])
				continue;
			double nagrada=0;
			/*try{
				nagrada=Neuronska_mreza.mreza.output(istorija1, h-20, 2);
			}catch (Exception e){}*
			int u=(int)(istorija1.ulogriver*20);
			int n=(int)(istorija1.novacriver*20);
			int v;
			for(v=0;v<40;v++)
				if(podelaverovatnoca[v]>=verovatnocariver)
					break;
			v--;
			if(u==20)
				u--;
			if(n==20)
				n--;
			if(v<0)
				v=0;
			nagrada=Qriver[v][u][n][h];
			if(nagrada>max)
			{	max=nagrada;
				indmax=h;
			}
			if(h>1&&(nagrada<0.1||Ruka.brojac.get(h+20)<=3000)||h>2)
				break;
			else
				max=max;
		}
		//if(h<9)
		//	h=9;
		Random rand = new Random();
		float random=rand.nextFloat();
		
		//float random=(float)(rand()%1000)/1000;
		if(random<1.1||max==Double.NEGATIVE_INFINITY)//biramo random akciju
			akcijariver=randomakcija(ulog,novac,h);
		else
		{   if(indmax==1||1.0/fibonaci[indmax]>(float)ulog/novac&&(float)ulog/novac>1.0/fibonaci[indmax+1])//call
				akcijariver=indmax;
			else//bet/raise
			{   if(ulog==0)
					akcijariver=indmax;
				else if(1.0/fibonaci[indmax]>2*(float)ulog/novac)//po pravilima ne moze da se digne za manje od toga
					akcijariver=indmax;
				else
					akcijariver=1;
			}
		}
		
		//IZBACITI SLEDECI DEO:
		/*if(verovatnocariver>=1-0.9/brojigraca)
			indmax=9;
		else
			indmax=1;
		random=10;
		max=0;
		if(akcijariver>1)
			akcijariver=9;*
		//KRAJ IZBACIVANJA
		
		akcijeriver.add(akcijariver);
		istorija1.akcija=akcijariver;
		
		if(akcijariver==0)
		{   if(ulog<(float)novac/50)
			{   dosadasnjiulog+=ulog;
				akcijariver=1;
				istorija1.akcija=1;
				akcijeriver.set(akcijeriver.size()-1,1);
				return ulog;
			}
			else
				return -1;
		}
		if(akcijariver==1||akcijariver==10)
		{   if(novac<ulog)
				ulog=novac;
			dosadasnjiulog+=ulog;
			return ulog;
		}
		int akcija=novac/fibonaci[akcijariver];
		if(akcija>najvisenovca)
			akcija=najvisenovca;
		dosadasnjiulog+=akcija;
		return akcija;
	}
	*/
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
    
	//MASINSKO UCENJE:

	//public List<Istorija> istorija;
	public int pocetninovac,novacflop,novacturn,novacriver,dosadasnjiulog;
	//Sto se tice biranja akcije na random: Jaci call i bet se mogu birati samo ako su slabiji call i bet dobra ideja.
	//ispod 100 call, a iznad bet.
	//Stanje pre flopa su samo karta1 i karta 2. Ovde karta e {A,K,Q,J,10,8-9,6-7,2-5}, obojene i neobojene.
	//Ukupno ih ima 8*8*2;
	public static double Q[][][]=new double[9][8][10], Qboja[][][]=new double[8][8][10];//prva dimenzija je 1. karta, 2. druga karta, a 3. akcije
	public static int Qbrojac[][][]=new int[9][8][10],Qbojabrojac[][][]=new int[8][8][10];
	public int q;//jacina dve dobijene karte (broj iz jedne od gornjih matrica); ali jacina po redu kod svih igraca
	public double verovatnoca[]=new double[3];
	public double Ulog[]=new double[3],Novac[]=new double[3];
	//Pošto je plan sa neuronskim mrežama propao, atributi verovatnoca, novac i ulog ce se diskretizovati.
	public static double Qflop[][][][]=new double[40][20][20][10];//prva dimenzija je verovatnoća, 2. novac, a 3. ulog, a 4. akcija
	public static double Qturn[][][][]=new double[40][20][20][10],Qriver[][][][]=new double[40][20][20][10];
	public static double podelaverovatnoca[]=new double[40];
	
	public int akcijastart,akcija[]=new int[3];
	/*public List<Integer> akcijestart;//Vektor je jer vise puta mozemo doci na red da pratimo ulog.
	public List<Integer> akcijeflop;
	public List<Integer> akcijeturn;
	public List<Integer> akcijeriver;*/
	public static final int fibonaci[]={0,89,55,34,21,13,8,5,3,1,1};
	public static double alfastart,alfa=0.001,gama=1;

	public double ucenjepreflopa()
	{   //Stanje pre flopa su samo karta1 i karta 2. Ovde karta e {A,K,Q,J,10,8-9,6-7,2-5}, obojene i neobojene.
		int k1=0,k2=0;
		double nagrada=novac-pocetninovac;
		/*double znak=(Math.copySign(1.0,novac-pocetninovac)+1)/2;
		if(novac==pocetninovac)
			znak=0;
		if(novacflop<=0)
			nagrada=(novac-pocetninovac);
		else
			nagrada=(potpreflop*znak+novacflop-pocetninovac);*/
		if(turnir.ulog!=0)
			nagrada*=100.0/turnir.ulog/Turnir.bigblind*turnir.pocetnibigblind;
		else
			nagrada*=100.0/buyin;
		/*if(nagrada<-100||nagrada>100)
			return;*/
		if(nagrada==0)
			return 0;
		if(karta1.id<=5)
			k1=0;
		else if(karta1.id<=7)
			k1=1;
		else if(karta1.id<=9)
			k1=2;
		else if(karta1.id<=10)
			k1=3;
		else if(karta1.id<=11)
			k1=4;
		else if(karta1.id<=12)
			k1=5;
		else if(karta1.id<=13)
			k1=6;
		else
			k1=7;
		if(karta2.id<=5)
			k2=0;
		else if(karta2.id<=7)
			k2=1;
		else if(karta2.id<=9)
			k2=2;
		else if(karta2.id<=10)
			k2=3;
		else if(karta2.id<=11)
			k2=4;
		else if(karta2.id<=12)
			k2=5;
		else if(karta2.id<=13)
			k2=6;
		else
			k2=7;
		if(k1<k2)
		{	int p=k1;
			k1=k2;
			k2=p;
		}
		if(akcijastart==0)//fold uvek ima istu vrednost
		{	Q[0][0][0]+=alfa*(nagrada-Q[0][0][0]);
			Qbrojac[0][0][0]++;
		}
		//for(int akcijastart:akcijestart)
		else
		{	if(karta1.boja!=karta2.boja&&karta1.id!=karta2.id)
			{	alfa=1.0/(++Qbrojac[k1][k2][akcijastart]);
				if(alfa<0.01)
					alfa=0.01;
				Q[k1][k2][akcijastart]+=alfa*(nagrada-Q[k1][k2][akcijastart]);
				Qbrojac[k1][k2][akcijastart]++;
			}
			else if(karta1.id==karta2.id)
			{	alfa=1.0/(++Qbrojac[8][k2][akcijastart]);
				if(alfa<0.01)
					alfa=0.01;
				Q[8][k2][akcijastart]+=alfa*(nagrada-Q[8][k2][akcijastart]);
				Qbrojac[8][k2][akcijastart]++;
			}
			else
			{	alfa=1.0/(++Qbojabrojac[k1][k2][akcijastart]);
				if(alfa<0.01)
					alfa=0.01;
				Qboja[k1][k2][akcijastart]+=alfa*(nagrada-Qboja[k1][k2][akcijastart]);
				Qbojabrojac[k1][k2][akcijastart]++;
			}
			nagrada*=gama;
		}
		return nagrada;
	}
	
	public void upisinagradu(int najvisenovca1)
	{	double nagrada;
		/*double znak=(Math.copySign(1.0,novac-pocetninovac)+1)/2;
		if(istorija1.verturn==-10)
			nagrada=(potflop*znak+novacturn-pocetninovac);
		else if(istorija1.verriver==-10)
			nagrada=(potturn*znak+novacriver-pocetninovac);
		else*/
			nagrada=((float)novac-pocetninovac);
		/*if(nagrada<-99.0/100)
			nagrada=(-99.0/100);
		if(znak<0)
			nagrada=-(1/(1+nagrada))+1;*/
		if(turnir.ulog!=0)
			nagrada*=100.0/turnir.ulog/Turnir.bigblind*turnir.pocetnibigblind;
		else
			nagrada*=100.0/najvisenovca1;
		//if(Math.abs(nagrada)>20 && istorija1.akcija<2)
		//	nagrada=nagrada;
		//istorija.nagrada=(float)nagrada;

		int u,v,n;
		if(nagrada>400||nagrada<-100)
			n=0;
		for(int flop=0;flop<3;flop++)
		if(verovatnoca[flop]>-1)
		{	u=(int)(Ulog[flop]*20);
			n=(int)(Novac[flop]*20);
			for(v=0;v<40;v++)
				if(podelaverovatnoca[v]>=verovatnoca[flop])
					break;
			v--;
			if(u==20)
				u--;
			if(n==20)
				n--;
			if(v<0)
				v=0;
			if(flop==0)
				Qflop[v][u][n][akcija[0]]+=alfa*(nagrada-Qflop[v][u][n][akcija[0]]);
			else if(flop==1)
				Qturn[v][u][n][akcija[1]]+=alfa*(nagrada-Qturn[v][u][n][akcija[1]]);
			else
				Qriver[v][u][n][akcija[2]]+=alfa*(nagrada-Qriver[v][u][n][akcija[2]]);
			/*if(Qflop[v][u][n][akcija[0]]>500||Qturn[v][u][n][akcija[1]]>500||Qriver[v][u][n][akcija[2]]>500)
				v=0;
			if(Qflop[v][u][n][akcija[0]]<-100||Qturn[v][u][n][akcija[1]]<-100||Qriver[v][u][n][akcija[2]]<-100)
				v=0;*/
		}
	}
	
	@Override
	public String toString()
	{	
		return String.format("%-10s %7d$",ime,novac);
	}
	
    public boolean equals(Igrac igrac)
	{	if(this.ime==igrac.ime)
			if(this.karta1==igrac.karta1)
				if(this.karta2==igrac.karta2)
						return true;
		return false;
	}
}