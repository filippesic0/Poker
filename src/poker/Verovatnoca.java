package poker;

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
public class Verovatnoca
{	
	public static List<List<List<List<List<Float>>>>> verovatnoceflop;
	public static int parovipobedaflop[][]=new int[4][4];
	public static int paroviremiflop[][]=new int[4][4];
	public static int paroviporazflop[][]=new int[4][4];
	public static int parovipobedaturn[]=new int[4];
	public static int paroviremiturn[]=new int[4];
	public static int paroviporazturn[]=new int[4];
	public static int parovipobedariver[][]=new int[4][4];
	public static int paroviremiriver[][]=new int[4][4];
	public static int paroviporazriver[][]=new int[4][4];
	
	public static int rednibroj(Karta karta)
	{	return (karta.id-2+(karta.boja-1)*13);}
	
	public static int rednibroj(int broj,int boja)
	{	return (broj-2+(boja)*13);}
	
	public static Karta brojiboja(int rednibroj)
	{	return new Karta(rednibroj/13+1,rednibroj%13+2);}
	
	public static float Pflopturnriver(List<Igrac> igraci,Igrac naredu,int brojigraca,Karta karte[],int ind)
	{	int i,novac,Novac,parezbir=0;
		ArrayList<Integer> pare=new ArrayList<>();
		float verovatnoce[]=new float[brojigraca],ocekivanje=0;
		switch (ind) {
		case 3:
			verovatnoce=Pflop(brojigraca,karte);
			break;
		case 4:
			verovatnoce=Pturn(brojigraca,karte);
			break;
		case 5:
			verovatnoce=Priver(brojigraca,karte);
			break;
		}
		for(Igrac igrac:igraci)
			if(!igrac.fold&&igrac.novac>0&&!naredu.ime.equals(igrac.ime))
				pare.add(igrac.novac);
		for(i=0;i<pare.size();i++)
		{	if(pare.get(i)>naredu.novac)
				pare.set(i,naredu.novac);
			parezbir+=pare.get(i);
		}
		Collections.sort(pare);
		while(pare.size()>0)
		{	novac=pare.get(0);
			ocekivanje+=verovatnoce[pare.size()]*novac*pare.size();
			for(i=0;i<pare.size();i++)
			{	Novac=pare.get(i);
				pare.set(i,Novac-novac);
			}
			while(pare.size()>0&&pare.get(0)==0)
				pare.remove(0);
		}
		ocekivanje/=parezbir;
		return ocekivanje;
	}	
	
	public static float[] Pflop(int brojigraca, Karta karte[])//prve karte su u ruci, a ostale 3 na stolu
	{	int i,j,t,r,boja[]={0,0,0,0},maxboja=0,mnozilac,parovi[][],parovi1[][];
		Karta karte1[],karte2[],karte7[]=new Karta[7],karte5[]=new Karta[5];//7: svih 7 karata; 5: 5 karata na stolu
		Karta karta,karta1,karta2,karta3;
		//karte turna i rivera:
		ArrayList<Karta> Karte=new ArrayList<>(Arrays.asList(karte));
		float verovatnoce[]=new float[brojigraca];
		for(i=0;i<karte.length;i++)
		{	Karta kartaa=karte[i];
			if(i>=2)
			{	boja[kartaa.boja-1]++;
				if(boja[kartaa.boja-1]>boja[maxboja])
					maxboja=kartaa.boja-1;
			}
		}
		for(i=0;i<2;i++)
			karte7[i]=new Karta(karte[i]);
		for(;i<5;i++)
		{	karte7[i]=new Karta(karte[i]);
			karte5[i-2]=new Karta(karte[i]);
		}
		//Protivnik može da ima boju ako su 3 od 5 karata iste boje, pa se razlikuju slučajevi.
		//karte iste boje:
		for(t=3;t<15;t++)
		{	karta1=null;
			parovi=new int[4][4];
			karte1=new Karta[4];
			for(i=0;i<4;i++)
			{	karta=brojiboja(rednibroj(t,i));
				if(!karta.contains(Karte))
				{	karte1[i]=new Karta(karta);
					karta1=new Karta(karta);
					for(j=0;j<4;j++)
						parovi[i][j]=1;
				}
			}
			if(karta1==null)
				continue;
			for(r=2;r<t;r++)
			{	karta2=null;
				parovi1=new int[4][4];
				for(i=0;i<4;i++)
					for(j=0;j<4;j++)
						parovi1[i][j]=parovi[i][j];
				karte2=new Karta[4];
				for(i=0;i<4;i++)
				{	karta=brojiboja(rednibroj(r,i));
					if(!karta.contains(Karte))
					{	karte2[i]=new Karta(karta);
						karta2=new Karta(karta);
					}
					else
						for(j=0;j<4;j++)
							parovi1[j][i]=0;
				}
				if(karta2==null)
					continue;
				mnozilac=0;
				//boje koje nisu na stolu:
				for(i=0;i<4;i++)
					if(boja[i]==0&&parovi1[i][i]==1)
					{	mnozilac++;
						karta1=brojiboja(rednibroj(t,i));
						karta2=brojiboja(rednibroj(r,i));
					}
				if(mnozilac>0)
					Pflop1(karte7,karte5,karta1,karta2,mnozilac,brojigraca,verovatnoce);
				//boje koje su na stolu:
				for(i=0;i<4;i++)
					if(boja[i]>=1&&parovi1[i][i]==1)
					{	karta1=brojiboja(rednibroj(t,i));
						karta2=brojiboja(rednibroj(r,i));
						Pflop1(karte7,karte5,karta1,karta2,1,brojigraca,verovatnoce);
					}
			}
		}
		//karte razlicite boje:
		for(t=2;t<15;t++)
		{	karta1=null;
			parovi=new int[4][4];
			karte1=new Karta[4];
			for(i=0;i<4;i++)
			{	karta=brojiboja(rednibroj(t,i));
				if(!karta.contains(Karte))
				{	karte1[i]=new Karta(karta);
					karta1=new Karta(karta);
					for(j=0;j<4;j++)
						parovi[i][j]=1;
				}
			}
			if(karta1==null)
				continue;
			for(r=2;r<=t;r++)
			{	karta2=null;
				karta3=null;
				parovi1=new int[4][4];
				for(i=0;i<4;i++)
					for(j=0;j<4;j++)
						parovi1[i][j]=parovi[i][j];
				karte2=new Karta[4];
				for(i=0;i<4;i++)
				{	karta=brojiboja(rednibroj(r,i));
					if(!karta.contains(Karte))
					{	karte2[i]=new Karta(karta);
						karta2=karta3;
						karta3=new Karta(karta);
					}
					else
						for(j=0;j<4;j++)
							parovi1[j][i]=0;
				}
				if(karta2==null)
					if(karta3==null||karta3.equals(karta1))
						continue;
					else
						karta2=karta3;
				mnozilac=0;
				for(i=0;i<4;i++)
				{	if(boja[i]>=2)
						continue;
					for(j=0;j<4;j++)
					{	if(parovi1[i][j]==0)
							continue;
						if(i==j)
							continue;
						if(boja[j]>=2)
							continue;
						if(t==r&&j>i)
							break;
						mnozilac+=parovi1[i][j];
						karta1=brojiboja(rednibroj(t,i));
						karta2=brojiboja(rednibroj(r,j));
					}
				}
				if(mnozilac>0)
					Pflop1(karte7,karte5,karta1,karta2,mnozilac,brojigraca,verovatnoce);
				mnozilac=0;
				for(i=0;i<4;i++)
				{	if(boja[i]<2)
						continue;
					karta1=brojiboja(rednibroj(t,i));
					for(j=0;j<4;j++)
					{	if(i==j)
							continue;
						if(parovi1[i][j]==0)
							continue;
						mnozilac+=parovi1[i][j];
						karta2=brojiboja(rednibroj(r,j));
					}
					if(mnozilac>0)
						Pflop1(karte7,karte5,karta1,karta2,mnozilac,brojigraca,verovatnoce);
					break;
				}
				if(t==r)
					continue;
				mnozilac=0;
				for(i=0;i<4;i++)
				{	if(boja[i]<2)
						continue;
					karta2=brojiboja(rednibroj(r,i));
					for(j=0;j<4;j++)
					{	if(i==j)
							continue;
						if(parovi1[j][i]==0)
							continue;
						mnozilac+=parovi1[j][i];
						karta1=brojiboja(rednibroj(t,j));
					}
					if(mnozilac>0)
						Pflop1(karte7,karte5,karta1,karta2,mnozilac,brojigraca,verovatnoce);
					break;
				}
			}
		}
		for(i=1;i<brojigraca;i++)
			verovatnoce[i]=verovatnoce[i]/1081;
		return verovatnoce;
	}
	
	public static void Pflop1(Karta karte7[],Karta karte5[],Karta karta1,Karta karta2,int mnozilac,int brojigraca,float verovatnoce[])
	{	karte7[5]=new Karta(karta1);
		karte5[3]=new Karta(karta1);
		karte7[6]=new Karta(karta2);
		karte5[4]=new Karta(karta2);
		int rezultat[];
		float verovatnoca[]=new float[brojigraca];
		Karta ruka7[];
		ruka7=ruka(karte7);
		//ručne karte koje učestvuju u formiranju ruke:
		karta1=new Karta(karte7[0]);
		karta2=new Karta(karte7[1]);
		rezultat=Protivnikovekarte(karte5,ruka7,karta1,karta2);
		verovatnoca[1]=((float)rezultat[2]+rezultat[1]/2)/990;
		for(int i=2;i<brojigraca;i++)
			verovatnoca[i]=verovatnoca[i-1]*verovatnoca[1];
		for(int i=1;i<brojigraca;i++)
			verovatnoce[i]+=verovatnoca[i]*mnozilac;
	/*	
		karta1=new Karta(karte7[5]);
		karta2=new Karta(karte7[6]);
		verovatnoce[karta1.id][karta2.id]+=verovatnoca*mnozilac;
		verovatnoce[karta2.id][karta1.id]+=verovatnoca*mnozilac;
	*/	
	}
		
	public static float[] Pturn(int brojigraca, Karta karte[])//prve karte su u ruci, a ostale 4 na stolu
	{	int i,t,boja[]={0,0,0,0},maxboja=0,mnozilac,parovi[];
		Karta karte1[],karte7[]=new Karta[7],karte5[]=new Karta[5];//7: svih 7 karata; 5: 5 karata na stolu
		Karta karta,karta1;
		//karte turna i rivera:
		ArrayList<Karta> Karte=new ArrayList<>(Arrays.asList(karte));
		float verovatnoce[]=new float[brojigraca];
		for(i=0;i<karte.length;i++)
		{	Karta kartaa=karte[i];
			if(i>=2)
			{	boja[kartaa.boja-1]++;
				if(boja[kartaa.boja-1]>boja[maxboja])
					maxboja=kartaa.boja-1;
			}
		}
		for(i=0;i<2;i++)
			karte7[i]=new Karta(karte[i]);
		for(;i<6;i++)
		{	karte7[i]=new Karta(karte[i]);
			karte5[i-2]=new Karta(karte[i]);
		}
		//Protivnik može da ima boju ako su 4 od 5 karata iste boje, pa se razlikuju slučajevi.
		for(t=2;t<15;t++)
		{	karta1=null;
			parovi=new int[4];
			karte1=new Karta[4];
			for(i=0;i<4;i++)
			{	karta=brojiboja(rednibroj(t,i));
				if(!karta.contains(Karte))
				{	karte1[i]=new Karta(karta);
					karta1=new Karta(karta);
					parovi[i]=1;
				}
			}
			if(karta1==null)
				continue;
			mnozilac=0;
			//boje koje nisu 2 na stolu:
			for(i=0;i<4;i++)
				if(boja[i]<=1)
				{	if(parovi[i]==0)
						continue;
					mnozilac++;
					karta1=brojiboja(rednibroj(t,i));
				}
			if(mnozilac>0)
				Pturn1(karte7,karte5,karta1,mnozilac,brojigraca,verovatnoce);
			//boje koje su 2 na stolu:
			for(i=0;i<4;i++)
				if(boja[i]>=2)
				{	if(parovi[i]==0)
						continue;
					karta1=brojiboja(rednibroj(t,i));
					Pturn1(karte7,karte5,karta1,1,brojigraca,verovatnoce);
				}
		}
		for(i=1;i<brojigraca;i++)
			verovatnoce[i]=verovatnoce[i]/46;
		return verovatnoce;
	}
	
	public static void Pturn1(Karta karte7[],Karta karte5[],Karta karta1,int mnozilac,int brojigraca,float verovatnoce[])
	{	karte7[6]=new Karta(karta1);
		karte5[4]=new Karta(karta1);
		int rezultat[];
		Karta karta2,ruka7[];
		float verovatnoca[]=new float[brojigraca];
		ruka7=ruka(karte7);
		//ručne karte koje učestvuju u formiranju ruke:
		karta1=new Karta(karte7[0]);
		karta2=new Karta(karte7[1]);
		rezultat=Protivnikovekarte(karte5,ruka7,karta1,karta2);
		verovatnoca[1]=((float)rezultat[2]+rezultat[1]/2)/990;
		for(int i=2;i<brojigraca;i++)
			verovatnoca[i]=verovatnoca[i-1]*verovatnoca[1];
		for(int i=1;i<brojigraca;i++)
			verovatnoce[i]+=verovatnoca[i]*mnozilac;
	/*	
		karta1=new Karta(karte7[5]);
		karta2=new Karta(karte7[6]);
		verovatnoce[karta1.id][karta2.id]+=verovatnoca*mnozilac;
		verovatnoce[karta2.id][karta1.id]+=verovatnoca*mnozilac;
	*/	
	}
	
	public static float[] Priver(int brojigraca, Karta karte[])//prve karte su u ruci, a ostale 4 na stolu
	{	int rezultat[],i;
		Karta karta1,karta2,ruka7[];
		Karta karte7[]=new Karta[7],karte5[]=new Karta[5];//7: svih 7 karata; 5: 5 karata na stolu
		float verovatnoca[]=new float[brojigraca];
		for(i=0;i<2;i++)
			karte7[i]=new Karta(karte[i]);
		for(;i<7;i++)
		{	karte7[i]=new Karta(karte[i]);
			karte5[i-2]=new Karta(karte[i]);
		}
		ruka7=ruka(karte7);
		//ručne karte koje učestvuju u formiranju ruke:
		karta1=new Karta(karte7[0]);
		karta2=new Karta(karte7[1]);
		rezultat=Protivnikovekarte(karte5,ruka7,karta1,karta2);
		verovatnoca[1]=((float)rezultat[2]+rezultat[1]/2)/990;
		for(i=2;i<brojigraca;i++)
			verovatnoca[i]=verovatnoca[i-1]*verovatnoca[1];
		return verovatnoca;
	}
		
	public static int[] Protivnikovekarte(Karta karte[],Karta ruka7[],Karta Karta1,Karta Karta2)
	{	//Karta t,r;//karte u ruci, flop, turn i river
		int i,j,k1,k2,boja[]={0,0,0,0},maxboja=0,mnozilac,rezultat[]=new int[3];
		Karta karte1[],karte2[],karte7[]=new Karta[7];//7: svih 7 karata; 5: 5 karata na stolu
		Karta karta1,karta2;
		Karta spil[][]=new Karta[15][6];
		int spil1[]=new int[15];
		ArrayList<Karta> Karte=new ArrayList<>(Arrays.asList(karte));
		Karte.add(new Karta(Karta1));
		Karte.add(new Karta(Karta2));
		for(Karta kartaa:karte)
		{	boja[kartaa.boja-1]++;
			if(boja[kartaa.boja-1]>boja[maxboja])
				maxboja=kartaa.boja-1;
		}
		for(i=0;i<5;i++)
			karte7[i]=new Karta(karte[i]);
		for(i=2;i<15;i++)
			for(j=0;j<4;j++)
			{	karta1=brojiboja(rednibroj(i,j));
				if(!karta1.contains(Karte)&&boja[j]<4)
				{	spil[i][j]=karta1;
					spil[i][5]=spil[i][4];
					spil[i][4]=karta1;
					spil1[i]++;
				}
			}
		//nije boja:
		if(boja[maxboja]<5)
			for(k1=2;k1<15;k1++)
			{	karta1=spil[k1][4];
				karte1=spil[k1];
				if(karta1==null)
					continue;
				for(k2=2;k2<=k1;k2++)
				{	i=spil1[k1];
					mnozilac=i*(i-1)/2;
					j=spil1[k2];
					if(k2<k1)
					{	karte2=spil[k2];
						karta2=spil[k2][4];
						if(karta2==null)
							continue;
						mnozilac=i*j;
						if(boja[maxboja]==3&&karte1[maxboja]!=null&&karte2[maxboja]!=null)
							mnozilac--;
					}
					else
						karta2=spil[k2][5];
					if(mnozilac==0)
						continue;
					if(karta2==null)
						continue;
					karte7[5]=new Karta(karta1);
					karte7[6]=new Karta(karta2);
					Protivnikovekarte1(karte7,ruka7,mnozilac,rezultat,false);
				}
			}
		//boja:
		if(boja[maxboja]>=3)//dve karte najzastupljenije boje
			for(k1=3;k1<15;k1++)
			{	karta1=brojiboja(rednibroj(k1,maxboja));
				if(karta1.contains(Karte))
					continue;
				for(k2=2;k2<k1;k2++)
				{	karta2=brojiboja(rednibroj(k2,maxboja));
					if(karta2.contains(Karte))
						continue;
					karte7[5]=new Karta(karta1);
					karte7[6]=new Karta(karta2);
					Protivnikovekarte1(karte7,ruka7,1,rezultat,true);
				}
			}
		if(boja[maxboja]>=4)//1 karta najzastupljenije boje
			for(k1=2;k1<15;k1++)
			{	karta1=brojiboja(rednibroj(k1,maxboja));
				if(karta1.contains(Karte))
					continue;
				for(k2=2;k2<15;k2++)
				{	karta2=spil[k2][4];
					if(karta2==null)
						continue;
					mnozilac=spil1[k2];
					if(mnozilac==0)
						continue;
					karte7[5]=new Karta(karta1);
					karte7[6]=new Karta(karta2);
					Protivnikovekarte1(karte7,ruka7,mnozilac,rezultat,true);
				}
			}
		
		if(boja[maxboja]>=5)//sve ostale karte
		{	mnozilac=0;
			for(k1=2;k1<15;k1++)
			{	karta1=spil[k1][4];
				if(karta1==null)
					continue;
				for(k2=2;k2<=k1;k2++)
				{	i=spil1[k1];
					j=spil1[k2];
					if(k2<k1)
					{	karta2=spil[k2][4];
						if(karta2==null)
							continue;
						mnozilac+=i*j;
					}
					else
					{	karta2=spil[k2][5];
						mnozilac+=i*(i-1)/2;
					}
					if(karta2==null)
						continue;
					karte7[5]=new Karta(karta1);
					karte7[6]=new Karta(karta2);
				}
			}
			Protivnikovekarte1(karte7,ruka7,mnozilac,rezultat,true);
		}
		return rezultat;
	}
	
	public static void Protivnikovekarte1(Karta karte[],Karta ruka7[],int mnozilac,int rezultat[],boolean boja)
	{	int rez;
		Karta ruka[];
		if(boja)
			ruka=rukaboja(karte);
		else
			ruka=rukabezboje(karte);
		rez=Ruka.poredjenje(ruka7,ruka);
		rezultat[rez+1]+=mnozilac;
	}
	
	public static Karta[] ruka(Karta[] karte1)
	{	Karta ruka[]=new Karta[6];
		int n=karte1.length,boja[]={0,0,0,0,0},maxboja=0;
		List<Karta> obojenekarte=new ArrayList<>();
		List<List<Karta>> istekarte=new ArrayList<>();
		Karta karte[]=new Karta[n];
		int i,j;
		for(i=0;i<n;i++)
			karte[i]=new Karta(karte1[i]);
		Arrays.sort(karte);
		for(Karta karta:karte)
			boja[karta.boja]++;
		for(i=0;i<5;i++)
			if(boja[i]>4)
			{	maxboja=boja[i];
				break;
			}
		//boja:
		int kentauboji=0;
		Karta kenta[] = new Karta[5];
		if(i<5&&maxboja>=5)
		{	for(Karta karta:karte)
				if(karta.boja==i)
					obojenekarte.add(karta);
			//kenta u boji:
			for(i=4;i<maxboja;i++)
				if(obojenekarte.get(i-4).id-obojenekarte.get(i).id==4)
				{	kentauboji=1;
					for(j=0;j<5;j++)
						kenta[j]=obojenekarte.get(i-4+j);
					break;
				}
			maxboja--;
			if(obojenekarte.get(0).id==14&&obojenekarte.get(maxboja-3).id==5&&obojenekarte.get(maxboja-4).id!=6&&obojenekarte.get(maxboja).id==2)
			{	
				for(j=0;j<4;j++)
					kenta[j]=obojenekarte.get(maxboja-3+j);
				kenta[4]=obojenekarte.get(0);
				kentauboji=1;
			}
			while(obojenekarte.size()>5)
				obojenekarte.remove(obojenekarte.size()-1);
		}
		for(i=0,j=0;i<n;i++)
		{	if(i>0&&karte[i-1].id==karte[i].id)
				istekarte.get(j-1).add(karte[i]);
			else
			{   istekarte.add(new ArrayList<>());
				istekarte.get(j).add(karte[i]);
				j++;
			}
		}
		int s=istekarte.size();
		//kenta:
		if(s>=5&&kentauboji==0)
		{	for(i=4;i<s;i++)
				if(istekarte.get(i-4).get(0).id-istekarte.get(i).get(0).id==4)
				{	for(j=0;j<5;j++)
						kenta[j]=istekarte.get(i-4+j).get(0);
					break;
				}
			
			i--;
			if(istekarte.get(0).get(0).id==14&&istekarte.get(i-3).get(0).id==5&&istekarte.get(i-4).get(0).id==6&&istekarte.get(i).get(0).id==2)
				i++;
			if(istekarte.get(0).get(0).id==14&&istekarte.get(i-3).get(0).id==5&&istekarte.get(i-4).get(0).id!=6&&istekarte.get(i).get(0).id==2)
			{	
				for(j=0;j<4;j++)
					kenta[j]=istekarte.get(i-3+j).get(0);
				kenta[4]=istekarte.get(0).get(0);
			}
		}
		//sortiranje:
		for(i=0;i<s-1;i++)
		{   int max=0,max1=0,indmax=0;
			for(j=i+1;j<s;j++)
			{   if(istekarte.get(j).size()>max||(istekarte.get(j).size()==max&&istekarte.get(j).get(0).id>max1))
				{   max=istekarte.get(j).size();
					max1=istekarte.get(j).get(0).id;
					indmax=j;
				}
			}
			if(max>istekarte.get(i).size()||(max==istekarte.get(i).size()&&max1>istekarte.get(i).get(0).id))
			   Collections.swap(istekarte,i,indmax);
		}
		int S=0;
		for(i=0;i<istekarte.size()&&i<3;i++)
		{	int size=istekarte.get(i).size();
			if(size!=2&&size!=4)
				break;
			S+=size;
		}
		if(S==6)
		{	i=3-istekarte.get(0).size()/2;//za 4 i je 1, a za 2 je 2.
			try{
				if(istekarte.get(i+1).get(0).id>istekarte.get(i).get(0).id)
					Collections.swap(istekarte,i,i+1);
			}catch(Exception e){}
		}
		for(i=0,j=0;i<s-1;i++)
		{   if(j+istekarte.get(i).size()>=5)
			{   i++;
				j=i;
				for(;i<(int)istekarte.size();)
				   istekarte.remove(i);
				break;
			}
			else
				j+=istekarte.get(i).size();
		}
		//kenta u boji:
		if(kentauboji==1)
		{   ruka[0]=new Karta(0,8);
			for(i=0;i<5;i++)
				ruka[i+1]=kenta[i];
		}
		//poker:
		else if(istekarte.get(0).size()==4)
		{   ruka[0]=new Karta(0,7);
			for(i=0;i<4;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			ruka[5]=istekarte.get(1).get(0);
		}
		//full house:
		else if(istekarte.get(0).size()==3&&istekarte.get(1).size()>=2)
		{   ruka[0]=new Karta(0,6);
			for(i=0;i<3;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<2;i++)
				ruka[i+4]=istekarte.get(1).get(i);
		}
		//boja:
		else if(!obojenekarte.isEmpty())
		{   ruka[0]=new Karta(0,5);
			for(i=0;i<5;i++)
				ruka[i+1]=obojenekarte.get(i);
		}
		//kenta:
		else if(kenta[1]!=null)
		{   ruka[0]=new Karta(0,4);
			for(i=0;i<5;i++)
				ruka[i+1]=kenta[i];
		}
		//triling:
		else if(istekarte.get(0).size()==3)
		{   ruka[0]=new Karta(0,3);
			for(i=0;i<3;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			ruka[4]=istekarte.get(1).get(0);
			ruka[5]=istekarte.get(2).get(0);
		}
		//2 para:
		else if(istekarte.get(0).size()==2&&istekarte.get(1).size()==2)
		{   ruka[0]=new Karta(0,2);
			for(i=0;i<2;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<2;i++)
				ruka[i+3]=istekarte.get(1).get(i);
			ruka[5]=istekarte.get(2).get(0);
		}
		//par:
		else if(istekarte.get(0).size()==2)
		{   ruka[0]=new Karta(0,1);
			for(i=0;i<2;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<3;i++)
				ruka[i+3]=istekarte.get(i+1).get(0);
		}
		//pet dasaka
		else
		{   ruka[0]=new Karta(0,0);
			try{
			for(i=0;i<5;i++)
				ruka[i+1]=istekarte.get(i).get(0);
			}catch(Exception e){
				i++;
			}
		}
		return ruka;
	}
	
	public static Karta[] rukabezboje(Karta[] karte1)
	{	Karta ruka[]=new Karta[6];
		int n=karte1.length;
		List<List<Karta>> istekarte=new ArrayList<>();
		Karta karte[]=new Karta[n];
		int i,j;
		for(i=0;i<n;i++)
			karte[i]=new Karta(karte1[i]);
		Arrays.sort(karte);
		Karta kenta[] = new Karta[5];
		for(i=0,j=0;i<n;i++)
		{	if(i>0&&karte[i-1].id==karte[i].id)
				istekarte.get(j-1).add(karte[i]);
			else
			{   istekarte.add(new ArrayList<>());
				istekarte.get(j).add(karte[i]);
				j++;
			}
		}
		int s=istekarte.size();
		//kenta:
		if(s>=5)
		{	for(i=4;i<s;i++)
				if(istekarte.get(i-4).get(0).id-istekarte.get(i).get(0).id==4)
				{	for(j=0;j<5;j++)
						kenta[j]=istekarte.get(i-4+j).get(0);
					break;
				}
			
			i--;
			if(istekarte.get(0).get(0).id==14&&istekarte.get(i-3).get(0).id==5&&istekarte.get(i-4).get(0).id==6&&istekarte.get(i).get(0).id==2)
				i++;
			if(istekarte.get(0).get(0).id==14&&istekarte.get(i-3).get(0).id==5&&istekarte.get(i-4).get(0).id!=6&&istekarte.get(i).get(0).id==2)
			{	
				for(j=0;j<4;j++)
					kenta[j]=istekarte.get(i-3+j).get(0);
				kenta[4]=istekarte.get(0).get(0);
			}
		}
		//sortiranje:
		for(i=0;i<s-1;i++)
		{   int max=0,max1=0,indmax=0;
			for(j=i+1;j<s;j++)
			{   if(istekarte.get(j).size()>max||(istekarte.get(j).size()==max&&istekarte.get(j).get(0).id>max1))
				{   max=istekarte.get(j).size();
					max1=istekarte.get(j).get(0).id;
					indmax=j;
				}
			}
			if(max>istekarte.get(i).size()||(max==istekarte.get(i).size()&&max1>istekarte.get(i).get(0).id))
			   Collections.swap(istekarte,i,indmax);
		}
		int S=0;
		for(i=0;i<istekarte.size()&&i<3;i++)
		{	int size=istekarte.get(i).size();
			if(size!=2&&size!=4)
				break;
			S+=size;
		}
		if(S==6)
		{	i=3-istekarte.get(0).size()/2;//za 4 i je 1, a za 2 je 2.
				if(istekarte.get(i+1).get(0).id>istekarte.get(i).get(0).id)
					Collections.swap(istekarte,i,i+1);
		}
		for(i=0,j=0;i<s-1;i++)
		{   if(j+istekarte.get(i).size()>=5)
			{   i++;
				j=i;
				for(;i<(int)istekarte.size();)
				   istekarte.remove(i);
				break;
			}
			else
				j+=istekarte.get(i).size();
		}
		//poker:
		if(istekarte.get(0).size()==4)
		{   ruka[0]=new Karta(0,7);
			for(i=0;i<4;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			ruka[5]=istekarte.get(1).get(0);
		}
		//full house:
		else if(istekarte.get(0).size()==3&&istekarte.get(1).size()>=2)
		{   ruka[0]=new Karta(0,6);
			for(i=0;i<3;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<2;i++)
				ruka[i+4]=istekarte.get(1).get(i);
		}
		//kenta:
		else if(kenta[1]!=null)
		{   ruka[0]=new Karta(0,4);
			for(i=0;i<5;i++)
				ruka[i+1]=kenta[i];
		}
		//triling:
		else if(istekarte.get(0).size()==3)
		{   ruka[0]=new Karta(0,3);
			for(i=0;i<3;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			ruka[4]=istekarte.get(1).get(0);
			ruka[5]=istekarte.get(2).get(0);
		}
		//2 para:
		else if(istekarte.get(0).size()==2&&istekarte.get(1).size()==2)
		{   ruka[0]=new Karta(0,2);
			for(i=0;i<2;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<2;i++)
				ruka[i+3]=istekarte.get(1).get(i);
			ruka[5]=istekarte.get(2).get(0);
		}
		//par:
		else if(istekarte.get(0).size()==2)
		{   ruka[0]=new Karta(0,1);
			for(i=0;i<2;i++)
				ruka[i+1]=istekarte.get(0).get(i);
			for(i=0;i<3;i++)
				ruka[i+3]=istekarte.get(i+1).get(0);
		}
		//pet dasaka
		else
		{   ruka[0]=new Karta(0,0);
			try{
			for(i=0;i<5;i++)
				ruka[i+1]=istekarte.get(i).get(0);
			}catch(Exception e){
				i++;
			}
		}
		return ruka;
	}
	
	public static Karta[] rukaboja(Karta[] karte1)
	{	Karta ruka[]=new Karta[6];
		int n=karte1.length,boja[]={0,0,0,0,0},maxboja=0;
		List<Karta> obojenekarte=new ArrayList<>();
		Karta karte[]=new Karta[n];
		int i,j;
		for(i=0;i<n;i++)
			karte[i]=new Karta(karte1[i]);
		Arrays.sort(karte);
		for(Karta karta:karte)
			boja[karta.boja]++;
		for(i=0;i<5;i++)
			if(boja[i]>4)
			{	maxboja=boja[i];
				break;
			}
		//boja:
		int kentauboji=0;
		Karta kenta[] = new Karta[5];
		if(i<5&&maxboja>=5)
		{	for(Karta karta:karte)
				if(karta.boja==i)
					obojenekarte.add(karta);
			//kenta u boji:
			for(i=4;i<maxboja;i++)
				if(obojenekarte.get(i-4).id-obojenekarte.get(i).id==4)
				{	kentauboji=1;
					for(j=0;j<5;j++)
						kenta[j]=obojenekarte.get(i-4+j);
					break;
				}
			maxboja--;
			if(obojenekarte.get(0).id==14&&obojenekarte.get(maxboja-3).id==5&&obojenekarte.get(maxboja-4).id!=6&&obojenekarte.get(maxboja).id==2)
			{	
				for(j=0;j<4;j++)
					kenta[j]=obojenekarte.get(maxboja-3+j);
				kenta[4]=obojenekarte.get(0);
				kentauboji=1;
			}
			while(obojenekarte.size()>5)
				obojenekarte.remove(obojenekarte.size()-1);
		}
		//kenta u boji:
		if(kentauboji==1)
		{   ruka[0]=new Karta(0,8);
			for(i=0;i<5;i++)
				ruka[i+1]=kenta[i];
		}
		//boja:
		else if(!obojenekarte.isEmpty())
		{   ruka[0]=new Karta(0,5);
			for(i=0;i<5;i++)
				ruka[i+1]=obojenekarte.get(i);
		}
		return ruka;
	}
}