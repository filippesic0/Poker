package poker;

import java.util.*;
import java.io.*;


/**
 *
 * @author fpesic
 */
public class Ruka
{	
	public static List<Igrac> igraci;
	public static List<Istorija> istorija=new ArrayList<>();
	public static List<List<Double>> parametri;
	//public static List<Boolean> nemoguci=Arrays.asList(true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true);
	public static List<Integer> brojac=Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
	public Spil spil;
	public static int dugme;
	public int smallblind,bigblind,naredu;
	public int najvisenovca,najvisenovca1;//prvo je drugi po redu, jer najvise toliko moze da se ulozi,
	//a drugo je stvarno najvise novca
	public int igraciuigri,Ukupanulog=0,dosadasnjiulog,dosadasnjiulog1;
	public List<Integer> ukupanulog,granicauloga;
	public List<Igrac> delilaculoga;
	public Karta f1,f2,f3,t,r;
	public int pracenje;
	
	public Ruka(List<Igrac> i,int d,int s,int b,int p)
	{	igraci=i;
		dugme=d;
		smallblind=s;
		bigblind=b;
		pracenje=p;
		ukupanulog=new ArrayList<>();
		granicauloga=new ArrayList<>();
		delilaculoga=new ArrayList<>();
		f1=new Karta();
		f2=new Karta();
		f3=new Karta();
		t=new Karta();
		r=new Karta();
	}
	
	void Ispisisisteme(List<List<List<Double>>> X1) throws IOException
	{	FileWriter fileWriter = new FileWriter("Sistem.txt");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		int h=10;
		for(int i=0;i<7;i++)
		{	for(int j=0;j<7;j++)
				printWriter.printf("%g*a%d+",X1.get(h).get(i).get(j),j);
			printWriter.println(0);
		}
		printWriter.close();
	}
	
	int sledeci(int naredu,int size)
	{   naredu++;
		if(naredu==size)
			naredu=0;
		return naredu;
	}
	
	public List<Igrac> igraj() throws IOException
	{	najvisenovca=0;
		najvisenovca1=1;
		igraciuigri=(int)igraci.size();
		spil=new Spil();
		spil.shuffle();
		spil.shuffle();
		//deljenje
		for(Igrac igrac:igraci)
		{	igrac.deal(spil.spil.get(0),spil.spil.get(1));
			/*if(spil.spil.get(0).id==14&&spil.spil[1].id==14)
				najvisenovca=0;*/
			spil.spil.remove(0);
			spil.spil.remove(0);
		}
		
		naredu=dugme+1;
		while(naredu>=(int)igraci.size())
			naredu-=igraci.size();
		//Ko ima najvise novca?
		for(Igrac igrac:igraci)
		{	igrac.novacflop=igrac.novac;
			if(igrac.karta1.id!=0)
				if(igrac.novac>najvisenovca1)
				{	najvisenovca=najvisenovca1;
					najvisenovca1=igrac.novac;
				}
				else if(igrac.novac>najvisenovca)
					najvisenovca=igrac.novac;
			igrac.dosadasnjiulog=0;
		}
		int Sledeci=sledeci(naredu,igraci.size());
		if(pracenje>0)
		{	System.out.printf("%s %s %s small blind  %d\n",igraci.get(naredu),igraci.get(naredu).karta1,igraci.get(naredu).karta2,Math.min(smallblind,igraci.get(naredu).novac));
			System.out.printf("%s %s %s big   blind  %d\n",igraci.get(Sledeci),igraci.get(Sledeci).karta1,igraci.get(Sledeci).karta2,Math.min(bigblind,igraci.get(Sledeci).novac));
		}
		if(igraci.get(naredu).novac<=smallblind&&igraci.get(Sledeci).novac<=bigblind)
		{	Igrac igracmin,igracmax;
			if(igraci.get(naredu).novac<igraci.get(Sledeci).novac)
			{	igracmin=igraci.get(naredu);
				igracmax=igraci.get(Sledeci);
			}
			else
			{	igracmax=igraci.get(naredu);
				igracmin=igraci.get(Sledeci);
			}
			ukupanulog.add(igracmin.novac*2);
			ukupanulog.add(igracmax.novac-igracmin.novac);
			ukupanulog.add(0);
			Ukupanulog=igraci.get(naredu).novac+igraci.get(Sledeci).novac;
			granicauloga.add(igracmin.novac);
			granicauloga.add(igracmax.novac);
			delilaculoga.add(igracmin);
			delilaculoga.add(igracmax);
		}
		else if(igraci.get(naredu).novac<=smallblind)
		{	ukupanulog.add(igraci.get(naredu).novac*2);
			ukupanulog.add(bigblind-igraci.get(naredu).novac);
			granicauloga.add(igraci.get(naredu).novac);
			Ukupanulog=igraci.get(naredu).novac+bigblind;
			delilaculoga.add(igraci.get(naredu));
		}
		else if(igraci.get(Sledeci).novac<=smallblind)
		{	ukupanulog.add(igraci.get(Sledeci).novac*2);
			ukupanulog.add(smallblind-igraci.get(Sledeci).novac);
			granicauloga.add(igraci.get(Sledeci).novac);
			Ukupanulog=igraci.get(Sledeci).novac+smallblind;
			delilaculoga.add(igraci.get(Sledeci));
		}
		else if(igraci.get(Sledeci).novac<=bigblind)
		{	ukupanulog.add(igraci.get(Sledeci).novac+smallblind);
			ukupanulog.add(0);
			granicauloga.add(igraci.get(Sledeci).novac);
			Ukupanulog=igraci.get(Sledeci).novac+smallblind;
			delilaculoga.add(igraci.get(Sledeci));
		}
		else
		{	ukupanulog.add(smallblind+bigblind);
			Ukupanulog=smallblind+bigblind;
		}
		granicauloga.add(bigblind);
		igraci.get(naredu).blind(smallblind);
		igraci.get(Sledeci).blind(bigblind);

		naredu=sledeci(naredu,igraci.size());
		naredu=sledeci(naredu,igraci.size());
		dosadasnjiulog1=0;
		ulaganje();
		Igrac.potpreflop=Ukupanulog;
		if(igraciuigri==1)
		/*{	pobednik1();
			return igraci;
		}*/
		{	Igrac igrac=igraci.get(0);
			for(int i=0;i<igraci.size();i++)
			{	igrac=igraci.get(i);
				if(igrac.karta1.id!=0)
					break;
			}
			for(int j=0;j<ukupanulog.size();j++)
				igrac.winner(ukupanulog.get(j),pracenje,1);
			//goto A;
		}
		else
		{	flop();
			if(najvisenovca>0)
				ulaganje();
			Igrac.potflop=Ukupanulog;
			if(igraciuigri==1)
				pobednik1();
				//goto A;
			else
			{	turn();
				if(najvisenovca>0)
					ulaganje();
				Igrac.potturn=Ukupanulog;
				if(igraciuigri==1)
					pobednik1();
					//goto A;
				else
				{	river();
					if(najvisenovca>0)
						ulaganje();
					if(igraciuigri==1)
						pobednik1();
						//goto A;
					else
						pobednik();
				}
			}
		}
/*A:*/	/*for (Igrac igrac:igraci)
		{	if(igrac.istorija.size()>0)
				for(Istorija istorija1:igrac.istorija)
					istorija.add(0,istorija1);
			igrac.istorija.clear();
		}*/
		return igraci;
	}
	
	public void ulaganje() throws IOException
	{	int brojbetova=0;
		for(int I=0;I<igraci.size();I++,naredu=sledeci(naredu,igraci.size()))
		{   Igrac igrac=igraci.get(naredu);
			dosadasnjiulog=igrac.dosadasnjiulog;
			double verovatnoca=-1;
			int a;
			if(igrac.karta1.id==0||igrac.novac==0)
				continue;
			if(f1.id==0)
				a=igrac.preflop(granicauloga.get(granicauloga.size()-1),najvisenovca,igraciuigri,brojbetova);
			else if(t.id==0)
			{	
				a=igrac.flopturnriver(0,igraci,granicauloga.get(granicauloga.size()-1),najvisenovca,najvisenovca1,igraciuigri,brojbetova,f1,f2,f3,null,null);
 				verovatnoca=igrac.istorija.verovatnoca[0];
			}
			else if(r.id==0)
			{	
				a=igrac.flopturnriver(1,igraci,granicauloga.get(granicauloga.size()-1),najvisenovca,najvisenovca1,igraciuigri,brojbetova,f1,f2,f3,t,null);
				verovatnoca=igrac.istorija.verovatnoca[1];
			}
			else
			{	
				a=igrac.flopturnriver(2,igraci,granicauloga.get(granicauloga.size()-1),najvisenovca,najvisenovca1,igraciuigri,brojbetova,f1,f2,f3,t,r);
				verovatnoca=igrac.istorija.verovatnoca[2];
			}
			//if(verovatnoca<0.05)
			//	verovatnoca=(float)0.05;
			//if(pracenje>0)
			//	System.out.printf("%s %6d$ %3s %3s\n",igrac.ime,igrac.novac,igrac.karta1,igrac.karta2);
			if(a<smallblind&&a>0)
			{	igrac.dosadasnjiulog-=a;
				a=0;
			}
			if(a==-1)
			{	if(pracenje>0)
					if(f1.id!=0)
						System.out.printf("%s %s %s %2d%% fold\n",igrac,igrac.karta1,igrac.karta2,(int)(verovatnoca*100));
					else
						System.out.printf("%s %s %s fold\n",igrac,igrac.karta1,igrac.karta2);
				igrac.fold(najvisenovca1);
				igraciuigri--;
				if(igraciuigri==1)
					return;
				//igraci.erase(igraci.get(0)+naredu);
				//i--;
				//naredu--;
			}
			/*else if(a==0)//check
				if(pracenje>0)
					if(f1.id!=0)
						System.out.printf("%s %s %s %2d%% check\n",igrac,igrac.karta1,igrac.karta2,(int)(verovatnoca*100));
					else
						System.out.printf("%s %s %s check\n",igrac,igrac.karta1,igrac.karta2);*/
			else if(a<granicauloga.get(granicauloga.size()-1)-dosadasnjiulog)
			{	int l=ukupanulog.size(),i;
				if(pracenje>0)
					if(f1.id!=0)
						System.out.printf("%s %s %s %2d%% all in %d\n",igrac,igrac.karta1,igrac.karta2,(int)(verovatnoca*100),igrac.novac);
					else
						System.out.printf("%s %s %s all in %d\n",igrac,igrac.karta1,igrac.karta2,igrac.novac);
				for(i=0;i<l;i++)
				{	ukupanulog.set(i,ukupanulog.get(i)+Math.max(granicauloga.get(i)-dosadasnjiulog,0));
					Ukupanulog+=a;
					a-=Math.max(granicauloga.get(i)-dosadasnjiulog,0);
					dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i),0);
					if(i>0)
					{	ukupanulog.set(i,ukupanulog.get(i)-Math.max(granicauloga.get(i-1)-dosadasnjiulog,0));
						a+=Math.max(granicauloga.get(i-1)-dosadasnjiulog,0);
						dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i-1),0);
					}
					if(a<0)
					{	ukupanulog.set(i,ukupanulog.get(i)+a);
						break;
					}
				}
				granicauloga.add(i,igrac.novac+dosadasnjiulog1);
				ukupanulog.add(i+1,0);
				delilaculoga.add(i,igrac);
				for(Igrac igracc:igraci)
				{	ukupanulog.set(i,ukupanulog.get(i)-Math.max(igracc.dosadasnjiulog-granicauloga.get(i),0));
					ukupanulog.set(i+1,ukupanulog.get(i+1)+Math.max(igracc.dosadasnjiulog-granicauloga.get(i),0));
				}
				igrac.allin();
				brojbetova++;
			}
			else if(a==granicauloga.get(granicauloga.size()-1)-dosadasnjiulog)
			{	a=igrac.call(a);
				if(pracenje>0)
				{	if(f1.id!=0)
						System.out.printf("%s %s %s %2d%%",igrac,igrac.karta1,igrac.karta2,(int)(verovatnoca*100));
					else
						System.out.printf("%s %s %s",igrac,igrac.karta1,igrac.karta2);
					if(igrac.novac==0)
						System.out.printf(" all in %d\n",a+dosadasnjiulog-dosadasnjiulog1);
					else if(a>0)
						System.out.printf(" call   %d\n",a+dosadasnjiulog-dosadasnjiulog1);
					else
						System.out.printf(" check\n");
				}
				Ukupanulog+=a;
				granicauloga.set(granicauloga.size()-1,a+dosadasnjiulog);
				int l=ukupanulog.size()-1;
				for(int i=0;i<l;i++)
				{	ukupanulog.set(i,ukupanulog.get(i)+Math.max(granicauloga.get(i)-dosadasnjiulog,0));
					a-=Math.max(granicauloga.get(i)-dosadasnjiulog,0);
					dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i),0);
					if(i>0)
					{	ukupanulog.set(i,ukupanulog.get(i)-Math.max(granicauloga.get(i-1)-dosadasnjiulog,0));
						a+=Math.max(granicauloga.get(i-1)-dosadasnjiulog,0);
						dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i-1),0);
					}
				}
				ukupanulog.set(ukupanulog.size()-1,ukupanulog.get(ukupanulog.size()-1)+a);
				//ukupanulog.back()+=a;
				if(igrac.novac==0&&ukupanulog.get(ukupanulog.size()-1)>0)
				{	ukupanulog.add(0);
					granicauloga.add(granicauloga.get(granicauloga.size()-1));
					delilaculoga.add(igrac);
				}
			}
			//bet/raise
			else// if(a>granicauloga.get(granicauloga.size()-1)-dosadasnjiulog)
			{	I=0;
				if(a>najvisenovca)
				{	igrac.dosadasnjiulog-=a-najvisenovca;
					a=najvisenovca;
				}
				igrac.call(a);
				if(pracenje>0)
				{	if(f1.id!=0)
						System.out.printf("%s %s %s %2d%%",igrac,igrac.karta1,igrac.karta2,(int)(verovatnoca*100));
					else
						System.out.printf("%s %s %s",igrac,igrac.karta1,igrac.karta2);
					if(igrac.novac>0)
					{	if(granicauloga.get(granicauloga.size()-1)-dosadasnjiulog==0)
							System.out.printf(" bet    %d\n",a+dosadasnjiulog-dosadasnjiulog1);
						else
							System.out.printf(" raise  %d\n",a+dosadasnjiulog-dosadasnjiulog1);
					}
					else
						System.out.printf(" all in %d\n",a+dosadasnjiulog-dosadasnjiulog1);
				}
				Ukupanulog+=a;
				granicauloga.set(granicauloga.size()-1,a+dosadasnjiulog);
				int l=ukupanulog.size()-1;
				for(int i=0;i<l;i++)
				{	ukupanulog.set(i,ukupanulog.get(i)+Math.max(granicauloga.get(i)-dosadasnjiulog,0));
					a-=Math.max(granicauloga.get(i)-dosadasnjiulog,0);
					dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i),0);
					if(i>0)
					{	ukupanulog.set(i,ukupanulog.get(i)-Math.max(granicauloga.get(i-1)-dosadasnjiulog,0));
						a+=Math.max(granicauloga.get(i-1)-dosadasnjiulog,0);
						dosadasnjiulog=Math.max(dosadasnjiulog-granicauloga.get(i-1),0);
					}
				}
				ukupanulog.set(ukupanulog.size()-1,ukupanulog.get(ukupanulog.size()-1)+a);
				//ukupanulog.back()+=a;
				if(igrac.novac==0)
				{	ukupanulog.add(0);
					granicauloga.add(granicauloga.get(granicauloga.size()-1));
					delilaculoga.add(igrac);
				}
				if(brojbetova<2)
					brojbetova++;
			}
			/*if(pracenje>0)
				if(pracenje==1)
					System.in.read();
			else
				System.out.println();*/
		}
    }
	
	public void flop() throws IOException
	{	f1=spil.spil.get(0);
		spil.spil.remove(0);
		f2=spil.spil.get(0);
		spil.spil.remove(0);
		f3=spil.spil.get(0);
		spil.spil.remove(0);
		/*
		f1=new Karta(2,12);
		f2=new Karta(3,4);
		f3=new Karta(4,6);
		*/
		if(pracenje>0)
		{	System.out.printf("\nukupan ulog: %d\n %3s %3s %3s\n",Ukupanulog,f1,f2,f3);
			if(pracenje==2)
				System.in.read();
		}
			//getchar();
		/*else
			System.out.println();*/
		naredu=sledeci(dugme,igraci.size());
		najvisenovca=0;
		int p=0;
		///for(Igrac igrac:igraci)
		//Za promenljivu najvisenovca se bira drugi po redu igrac jer je to ono sto najvise moze da se ulozi:
		//ako najjaci igrac ulozi vise, vraca mu se visak.
		for (Igrac igrac:igraci)
		{	igrac.novacflop=igrac.novac;
			if(igrac.karta1.id!=0)
				if(igrac.novac>p)
				{
					najvisenovca=p;
					p=igrac.novac;
				}
				else if(najvisenovca==0)
					najvisenovca=igrac.novac;
		}
		dosadasnjiulog1=granicauloga.get(granicauloga.size()-1);
	}
	
	public void turn() throws IOException
	{	t=spil.spil.get(0);
		spil.spil.remove(0);
		
		//t=new Karta(1,3);
		
		if(pracenje>0)
		{	System.out.printf("\nukupan ulog: %d\n %3s %3s %3s %3s\n",Ukupanulog,f1,f2,f3,t);
			if(pracenje==2)
				System.in.read();
		}
		/*else
			System.out.println();*/
		naredu=sledeci(dugme,igraci.size());
		najvisenovca=0;
		int p=0;
		///for(Igrac igrac:igraci)
		for (Igrac igrac:igraci)
		{	igrac.novacturn=igrac.novac;
			if(igrac.karta1.id!=0)
				if(igrac.novac>p)
				{
					najvisenovca=p;
					p=igrac.novac;
				}
				else if(najvisenovca==0)
					najvisenovca=igrac.novac;
		}
		dosadasnjiulog1=granicauloga.get(granicauloga.size()-1);
	}
	
	public void river() throws IOException
	{	r=spil.spil.get(0);
		spil.spil.remove(0);
		
		//r=new Karta(2,10);
		
		if(pracenje>0)
		{	System.out.printf("\nukupan ulog: %d\n %3s %3s %3s %3s %3s\n",Ukupanulog,f1,f2,f3,t,r);
			if(pracenje==2)
				System.in.read();
		}
		/*else
			System.out.println();*/
		naredu=sledeci(dugme,igraci.size());
		najvisenovca=0;
		int p=0;
		for(Igrac igrac:igraci)
		{	igrac.novacriver=igrac.novac;
			if(igrac.karta1.id!=0)
				if(igrac.novac>p)
				{
					najvisenovca=p;
					p=igrac.novac;
				}
				else if(najvisenovca==0)
					najvisenovca=igrac.novac;
		}
		dosadasnjiulog1=granicauloga.get(granicauloga.size()-1);
	}
	
	public void pobednik() throws IOException
	{	if(pracenje>0)
			System.out.printf("\nukupan ulog: %d\n",Ukupanulog);
		//odredjivanje pobednika
		//int ruka[igraci.size()][6];
		List<List<Integer>> ruka=new ArrayList<>();
		//for(int i=0;i<igraci.size();i++)
		Karta niz[];
		int I=0;
		//int id[igraci.size()];
		List<Integer> id=new ArrayList<>();
		for(Igrac igrac:igraci)
		{	if(igrac.karta1.id!=0)
			{	niz=Verovatnoca.ruka(new Karta[]{igrac.karta1,igrac.karta2,f1,f2,f3,t,r});
				List<Integer> lista=new ArrayList<>();
				for(Karta niz1:niz)
					lista.add(niz1.id);
				ruka.add(lista);
				id.add(I);
			}
			I++;
		}
		//sortiranje
		for(int i=0;i<ruka.size()-1;i++)
		{	int max[]={0,0,0,0,0,0};
			int indmax=0;
			for(int j=i+1;j<ruka.size();j++)
			{   if(poredjenje(ruka.get(j),max)==1)
				{   for( int k=0;k<6;k++)
						max[k]=ruka.get(j).get(k);
					indmax=j;
				}
			}
			if(poredjenje(max,ruka.get(i))==1)
			{   Collections.swap(ruka,i,indmax);
				Collections.swap(id,i,indmax);
			}
		}
		for(int i=ukupanulog.size()-1;i>0;i--)
			if(ukupanulog.get(i)==0)
			{	ukupanulog.remove(i);
				delilaculoga.remove(i-1);
			}
		for(int i=0;i<ruka.size();i++)
		{   int jednaki=1;
			for(int j=i+1;j<ruka.size();j++)
				if(poredjenje(ruka.get(i),ruka.get(j))==0)
					jednaki++;
				else
					break;
			int j=0;
			/*igraci.get(id.get(i)).winner(ukupanulog.get(j)/jednaki);
			ukupanulog.set(j,ukupanulog.get(j)-ukupanulog.get(j)/jednaki);
			for(;j<delilaculoga.size();j++)
			{   
				if(igraci.get(id.get(i))==delilaculoga.get(j))
					break;
				igraci.get(id.get(i)).winner(ukupanulog.get(j+1)/jednaki);
				ukupanulog.set(j+1,ukupanulog.get(j+1)-ukupanulog.get(j+1)/jednaki);
			}*/
			Ukupanulog-=ukupanulog.get(j)/jednaki;
			igraci.get(id.get(i)).winner(ukupanulog.get(j)/jednaki,pracenje,0);
			ukupanulog.set(j,ukupanulog.get(j)-ukupanulog.get(j)/jednaki);
			for(;j<delilaculoga.size();j++)
			{
				if(igraci.get(id.get(i))==delilaculoga.get(j))
					break;
				igraci.get(id.get(i)).winner(ukupanulog.get(j+1)/jednaki,pracenje,0);
				ukupanulog.set(j+1,ukupanulog.get(j+1)-ukupanulog.get(j+1)/jednaki);
			}
			if(Ukupanulog<=0)
				break;
		}
		//double /*Nagrada=0,*/nagrada[]=new double[igraci.size()];
		int i=0;
		for(Igrac igrac:igraci)
		{	igrac.upisinagradu(najvisenovca1);
			/*nagrada[i]=*/igrac.ucenjepreflopa();
			//Nagrada+=nagrada[i];
			i++;
		}
		/*if(Math.abs(Nagrada)>0.001)
			for(Igrac igrac:igraci)
			{	igrac.ucenjepreflopa();
			}*/
	}
	
	public void pobednik1() throws IOException
	{	Igrac igrac=igraci.get(0);
		int i;
		for(i=0;i<igraci.size();i++)
		{	igrac=igraci.get(i);
			if(igrac.karta1.id!=0)
				break;
			igrac.upisinagradu(najvisenovca1);
		}
		for(int j=0;j<ukupanulog.size();j++)
			igrac.winner(ukupanulog.get(j),pracenje,1);
		for(;i<igraci.size();i++)
			igrac.upisinagradu(najvisenovca1);
	}
	
	public static int poredjenje(List<Integer> ruka,int[] max)
	{   for(int i=0;i<max.length;i++)
			if(ruka.get(i)>max[i])
				return 1;
			else if(ruka.get(i)<max[i])
				return -1;
		return 0;
	}
	
	public static int poredjenje(List<Integer> ruka,List<Integer> max)
	{   for(int i=0;i<max.size();i++)
			if(ruka.get(i)>max.get(i))
				return 1;
			else if(ruka.get(i)<max.get(i))
				return -1;
		return 0;
	}
	
	public static int poredjenje(int[] ruka,List<Integer> max)
	{   for(int i=0;i<max.size();i++)
			if(ruka[i]>max.get(i))
				return 1;
			else if(ruka[i]<max.get(i))
				return -1;
		return 0;
	}
	
	public static int poredjenje(Karta[] ruka,Karta[] max)
	{   for(int i=0;i<max.length;i++)
			if(ruka[i].id>max[i].id)
				return 1;
			else if(ruka[i].id<max[i].id)
				return -1;
		return 0;
	}
	
	public static float ocekivanje(Igrac naredu)
	{	int i,parezbir=0;
		ArrayList<Integer> pare=new ArrayList<>();
		for(Igrac igrac:Ruka.igraci)
			if(igrac.novac<naredu.novac)
			{	pare.add(igrac.novac);
				parezbir+=igrac.novac;
			}
			else
			{	pare.add(naredu.novac);
				parezbir+=naredu.novac;
			}
		parezbir-=naredu.novac;
		return (float)parezbir/naredu.novac;
	}
	
	public static float ocekivanje1(Igrac naredu)
	{	int i,parezbir=0;
		ArrayList<Integer> pare=new ArrayList<>();
		for(Igrac igrac:Ruka.igraci)
			if(igrac.karta1.id>0)
				if(igrac.novac<naredu.novac)
				{	pare.add(igrac.novac);
					parezbir+=igrac.novac;
				}
				else
				{	pare.add(naredu.novac);
					parezbir+=naredu.novac;
				}
		parezbir-=naredu.novac;
		return (float)parezbir/naredu.novac;
	}
	
	public static float pozicija(Igrac naredu,int preflop)
	{	int i,parezbir=0;
		ArrayList<Integer> pare=new ArrayList<>();
		boolean ind=false;
		for(i=dugme+1+preflop;i<=dugme+preflop;i++)
		{	if(i==igraci.size())
				i=0;
			Igrac igrac=igraci.get(i);
			if(igrac.ime.equals(naredu.ime))
				ind=true;
			if(!ind)
				continue;
			if(igrac.karta1.id>0)
				if(igrac.novac<naredu.novac)
				{	pare.add(igrac.novac);
					parezbir+=igrac.novac;
				}
				else
				{	pare.add(naredu.novac);
					parezbir+=naredu.novac;
				}
		}
		parezbir-=naredu.novac;
		return (float)parezbir/naredu.novac;
	}
};