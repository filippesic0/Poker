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
	public static int smallblind,bigblind,naredu;
	public int najvisenovca;//prvo je drugi po redu, jer najvise toliko moze da se ulozi,
	//a drugo je stvarno najvise novca
	public int igraciuigri,igracisaparama,Ukupanulog=0,ulogzapracenje=0;
	public Karta f1,f2,f3,t,r;
	public int pracenje;
	
	public Ruka(List<Igrac> i,int d,int s,int b,int p)
	{	igraci=i;
		dugme=d;
		smallblind=s;
		bigblind=b;
		pracenje=p;
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
		if(naredu>=size)
			naredu-=size;
		return naredu;
	}
	
	static int Sledeci(int naredu,int size)
	{   naredu++;
		if(naredu>=size)
			naredu-=size;
		return naredu;
	}
	
	//Za promenljivu najvisenovca se bira drugi po redu igrac jer je to ono sto najvise moze da se ulozi:
	//ako najjaci igrac ulozi vise, vraca mu se visak.
	public int najvisenovca()
	{	int najvisenovca1=0;
		najvisenovca=0;
		for(Igrac igrac:igraci)
		{	if(!igrac.fold)
				if(igrac.novac>najvisenovca1)
				{	najvisenovca=najvisenovca1;
					najvisenovca1=igrac.novac;
				}
				else if(igrac.novac>najvisenovca)
					najvisenovca=igrac.novac;
		}
		return najvisenovca;
	}
	
	public List<Igrac> igraj() throws IOException
	{	igraciuigri=(int)igraci.size();
		igracisaparama=igraciuigri;
		spil=new Spil();
		spil.shuffle();
		spil.shuffle();
		//deljenje
		for(Igrac igrac:igraci)
		{	igrac.deal(spil.spil.get(0),spil.spil.get(1));
			spil.spil.remove(0);
			spil.spil.remove(0);
			igrac.dosadasnjiulog=0;
			igrac.fold=false;
		}
		
		naredu=dugme+1;
		while(naredu>=(int)igraci.size())
			naredu-=igraci.size();
		//Ko ima najvise novca?
		najvisenovca=najvisenovca();
		
		ulogzapracenje=0;
		int a=Math.min(smallblind,igraci.get(naredu).novac);
		Ukupanulog+=a;
		ulogzapracenje=a;
		igraci.get(naredu).blind(smallblind);
		if(pracenje>0)
			System.out.printf("%s %s %s small blind  %d\n",igraci.get(naredu),igraci.get(naredu).karta1,igraci.get(naredu).karta2,a);
		naredu=sledeci(naredu,igraci.size());
		
		
		a=Math.min(bigblind,igraci.get(naredu).novac);
		Ukupanulog+=a;
		ulogzapracenje=Math.max(ulogzapracenje,a);
		igraci.get(naredu).blind(bigblind);
		if(pracenje>0)
			System.out.printf("%s %s %s big   blind  %d\n",igraci.get(naredu),igraci.get(naredu).karta1,igraci.get(naredu).karta2,a);
		naredu=sledeci(naredu,igraci.size());
		
		ulaganje();
		Igrac.potpreflop=Ukupanulog;
		if(igraciuigri==1)
		{	for(Igrac igrac:igraci)
				if(!igrac.fold)
				{	igrac.winner(Ukupanulog,pracenje,1);
					break;
				}
		}
		else
		{	flopturnriver(0);
			if(najvisenovca>0)
				ulaganje();
			Igrac.potflop=Ukupanulog;
			if(igraciuigri==1)
				pobednik1();
			else
			{	flopturnriver(1);
				if(najvisenovca>0)
					ulaganje();
				Igrac.potturn=Ukupanulog;
				if(igraciuigri==1)
					pobednik1();
				else
				{	flopturnriver(2);
					if(najvisenovca>0)
						ulaganje();
					if(igraciuigri==1)
						pobednik1();
					else
						pobednik();
				}
			}
		}
		return igraci;
	}
	
	public void ulaganje() throws IOException
	{	int brojbetova=0;
		for(int I=0;I<igraci.size();I++,naredu=sledeci(naredu,igraci.size()))
		{   if(igracisaparama<=1&&ulogzapracenje==0)
				return;
			Igrac igrac=igraci.get(naredu);
			int dosadasnjiulog=igrac.dosadasnjiulog;
			double verovatnoca=-1;
			int akcija;
			if(igrac.fold||igrac.novac==0)
				continue;
			if(ulogzapracenje<0)
			{	System.out.print("Ruka linija 171");
				System.exit(0);
				ulogzapracenje+=0;
			}
			if(f1.id==0)
				akcija=igrac.preflop(ulogzapracenje,najvisenovca,igraciuigri,brojbetova);
			else if(t.id==0)
			{	
				akcija=igrac.flopturnriver(0,igraci,ulogzapracenje,Ukupanulog,najvisenovca,igraciuigri,brojbetova,f1,f2,f3,null,null);
 				verovatnoca=igrac.istorija.verovatnoca[0];
			}
			else if(r.id==0)
			{	
				akcija=igrac.flopturnriver(1,igraci,ulogzapracenje,Ukupanulog,najvisenovca,igraciuigri,brojbetova,f1,f2,f3,t,null);
				verovatnoca=igrac.istorija.verovatnoca[1];
			}
			else
			{	
				akcija=igrac.flopturnriver(2,igraci,ulogzapracenje,Ukupanulog,najvisenovca,igraciuigri,brojbetova,f1,f2,f3,t,r);
				verovatnoca=igrac.istorija.verovatnoca[2];
			}
			if(pracenje>0)
				System.out.printf("%s %s %s ",igrac,igrac.karta1,igrac.karta2);
			//if(pracenje>0)
			//	System.out.printf("%s %6d$ %3s %3s\n",igrac.ime,igrac.novac,igrac.karta1,igrac.karta2);
		//	if(najvisenovca>=bigblind&&akcija<bigblind&&akcija>0&&akcija>=ulogzapracenje/*&&ulogzapracenje==dosadasnjiulog*/)
		//		akcija=0;
			igrac.dosadasnjiulog=akcija;
			if(akcija==-1)//fold
			{	if(pracenje>0)
					if(f1.id!=0)
						System.out.printf("%2d%% fold\n",(int)(verovatnoca*100));
					else
						System.out.printf("fold\n");
				igrac.fold();
				igraciuigri--;
				igracisaparama--;
				if(igraciuigri==1)
					return;
			}
			else if(akcija<ulogzapracenje)//all in kad nema dovoljno para
			{	if(akcija<igrac.novac)
				{	akcija=0;
					return;
				}
				igrac.allin();
				igracisaparama--;
				if(pracenje>0)
					if(f1.id!=0)
						System.out.printf("%2d%% all in %d\n",(int)(verovatnoca*100),akcija);
					else
						System.out.printf("all in %d\n",akcija);
				Ukupanulog+=akcija-dosadasnjiulog;
			}
			else if(akcija==ulogzapracenje)//call
			{	Ukupanulog+=akcija-dosadasnjiulog;
				igrac.call(akcija-dosadasnjiulog);
				if(pracenje>0)
				{	if(f1.id!=0)
						System.out.printf("%2d%% ",(int)(verovatnoca*100));
					if(igrac.novac==0)
					{	System.out.printf("all in %d\n",akcija);
						igracisaparama--;
					}
					else if(akcija>0)
						System.out.printf("call   %d\n",akcija);
					else
						System.out.printf("check\n");
				}
			}
			else //bet/raise
			{	I=0;
				if(akcija>najvisenovca)
				{	igrac.dosadasnjiulog-=akcija-najvisenovca;
					akcija=najvisenovca;
					System.out.print("Ruka linija 253");
					System.exit(0);
				}
				Ukupanulog+=akcija-dosadasnjiulog;
				igrac.call(akcija-dosadasnjiulog);
				if(pracenje>0)
				{	if(f1.id!=0)
						System.out.printf("%2d%% ",(int)(verovatnoca*100));
					if(igrac.novac==0)
					{	System.out.printf("all in %d\n",akcija);
						igracisaparama--;
					}
					else
					{	if(ulogzapracenje==0)
							System.out.printf("bet    %d\n",akcija);
						else
							System.out.printf("raise  %d\n",akcija);
					}
				}
				ulogzapracenje=akcija;
				brojbetova++;
				if(brojbetova>5)
					brojbetova+=0;
			}
			/*if(pracenje>0)
				if(pracenje==1)
					System.in.read();
			else
				System.out.println();*/
			Ukupanulog+=0;
			if(Ukupanulog<0)
			{	System.out.print("Ruka linija 285");
				System.exit(0);
			}	
			
		}
    }
	
	public void flopturnriver(int flop) throws IOException
	{	switch (flop)
		{	case 0:
				f1=spil.spil.get(0);
				spil.spil.remove(0);
				f2=spil.spil.get(0);
				spil.spil.remove(0);
				f3=spil.spil.get(0);
				spil.spil.remove(0);
			break;
			case 1:
				t=spil.spil.get(0);
				spil.spil.remove(0);
			break;
			case 2:
				r=spil.spil.get(0);
				spil.spil.remove(0);
		}
		
		if(pracenje>0)
		{	switch (flop)
			{	case 0:
					System.out.printf("\nukupan ulog: %d\n %3s %3s %3s\n",Ukupanulog,f1,f2,f3);
				break;
				case 1:
					System.out.printf("\nukupan ulog: %d\n %3s %3s %3s %3s\n",Ukupanulog,f1,f2,f3,t);
				break;
				case 2:
					System.out.printf("\nukupan ulog: %d\n %3s %3s %3s %3s %3s\n",Ukupanulog,f1,f2,f3,t,r);
			}
			if(pracenje==2)
				System.in.read();
		}
		/*else
			System.out.println();*/
		naredu=sledeci(dugme,igraci.size());
		ulogzapracenje=0;
		int p=0;
		for(Igrac igrac:igraci)
			igrac.dosadasnjiulog=0;
		najvisenovca=najvisenovca();
	}
	
	public void pobednik() throws IOException
	{	if(pracenje>0)
			System.out.printf("\nukupan ulog: %d\n",Ukupanulog);
		//odredjivanje pobednika
		List<List<Integer>> ruka=new ArrayList<>();
		Karta niz[];
		int I=0;
		int id[]=new int[igraciuigri];
		int ind=0;
		for(Igrac igrac:igraci)
		{	if(!igrac.fold)
			{	niz=Verovatnoca.ruka(new Karta[]{igrac.karta1,igrac.karta2,f1,f2,f3,t,r});
				List<Integer> lista=new ArrayList<>();
				for(Karta niz1:niz)
					lista.add(niz1.id);
				ruka.add(lista);
				id[ind]=I;
				ind++;
			}
			I++;
		}
		//sortiranje
		for(int i=0;i<ruka.size()-1;i++)
		{	int max[]={0,0,0,0,0,0};
			int indmax=0;
			for(int j=i+1;j<ruka.size();j++)
			{   if(poredjenje(ruka.get(j),max)==1)
				{   for(int k=0;k<6;k++)
						max[k]=ruka.get(j).get(k);
					indmax=j;
				}
			}
			if(poredjenje(max,ruka.get(i))==1)
			{   Collections.swap(ruka,i,indmax);
				int p=id[i];
				id[i]=id[indmax];
				id[indmax]=p;
			}
		}
		int maxulog=0;
		int ukupanulog[]=new int[igraci.size()];
		for(int j=0;j<igraci.size();j++)
		{	Igrac igrac=igraci.get(j);
			ukupanulog[j]=igrac.pocetninovac-igrac.novac;
			if(ukupanulog[j]>maxulog)
				maxulog=ukupanulog[j];
		}
		
		for(int i=0;i<ruka.size();i++)
		{   Ukupanulog=0;
			int jednaki=1;
			int ulog=ukupanulog[id[i]];
			for(int j=i+1;j<ruka.size();j++)
				if(poredjenje(ruka.get(i),ruka.get(j))==0)
					jednaki++;
				else
					break;
			for(int j=0;j<igraci.size();j++)
			{	int a=Math.min(ulog,ukupanulog[j])/jednaki;
				Ukupanulog+=a;
				ukupanulog[j]-=a;
			}
			if(Ukupanulog==0)
				break;
			igraci.get(id[i]).winner(Ukupanulog,pracenje,0);
		}
		
		for(Igrac igrac:igraci)
		{	
			igrac.ucenjepreflopa();
			igrac.ucenjeflopturnriver();
		}
	}
	
	public void pobednik1() throws IOException
	{	for(Igrac igrac:igraci)
		{	if(!igrac.fold)
				igrac.winner(Ukupanulog,pracenje,1);
			if(f1!=null)
				igrac.ucenjeflopturnriver();
			igrac.ucenjepreflopa();
		}
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
	{	int parezbir=0;
		for(Igrac igrac:Ruka.igraci)
			if(igrac.novac<naredu.novac)
				parezbir+=igrac.novac;
			else
				parezbir+=naredu.novac;
		parezbir-=naredu.novac;
		return (float)parezbir/naredu.novac;
	}
	
	public static float ocekivanje1(Igrac naredu)
	{	int parezbir=0;
		for(Igrac igrac:Ruka.igraci)
			if(!igrac.fold)
				if(igrac.novac<naredu.novac)
					parezbir+=igrac.novac;
				else
					parezbir+=naredu.novac;
		parezbir-=naredu.novac;
		return (float)parezbir/naredu.novac;
	}
	
	public static float pozicija(Igrac naredu,int preflop)
	{	int i,parezbir=0;
		int a=Sledeci(igraci.indexOf(naredu),igraci.size());
		int b=Sledeci(dugme+preflop,igraci.size());
		for(i=a;i!=b;i=Sledeci(i,igraci.size()))
		{	Igrac igrac=igraci.get(i);
			if(igrac.ime.equals(naredu.ime))
				continue;
			if(!igrac.fold)
				if(igrac.novac<naredu.novac)
					parezbir+=igrac.novac;
				else
					parezbir+=naredu.novac;
		}
		return (float)parezbir/naredu.novac;
	}
};