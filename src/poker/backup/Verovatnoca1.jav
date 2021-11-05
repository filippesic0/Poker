package poker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
	
	public static int rednibroj(Karta karta)
	{	return (karta.id-2+(karta.boja-1)*13);}
	
	public static int rednibroj(int broj,int boja)
	{	return (broj-2+(boja)*13);}
	
	public static Karta brojiboja(int rednibroj)
	{	return new Karta(rednibroj/13+1,rednibroj%13+2);}
	
	public static float Pflop(int brojigraca, Karta karte[]) throws IOException//prve karte su u ruci, a ostale 3 na stolu
	{	//Karta t,r;//karte u ruci, flop, turn i river
		int rezultat[]=new int[3],i,j,t,r,boja[]={0,0,0,0},maxboja=0,mnozilac=0,parovi[][];
		boolean Boja;
		Karta karte1[]=new Karta[4],karte2[]=new Karta[4],karte7[]=new Karta[7],karte5[]=new Karta[5];//7: svih 7 karata; 5: 5 karata na stolu
		Karta karta,karta1,karta2,karta3=new Karta();
		Arrays.sort(karte);
        Main.time();
		//sa kojim kartama na turnu i riveru se poboljšava ruka i u koju:
    	//lista=Poboljsavanje2karte(ruka,karte,parovi);
		
		//za koje karte protivnika se remizira ili gubi:
		ArrayList<Karta> Karte=new ArrayList<>(Arrays.asList(karte));
		for(Karta kartaa:karte)
		{	boja[kartaa.boja-1]++;
			if(boja[kartaa.boja-1]>boja[maxboja])
				maxboja=kartaa.boja-1;
		}
		for(i=0;i<2;i++)
			karte7[i]=new Karta(karte[i]);
		for(;i<5;i++)
		{	karte7[i]=new Karta(karte[i]);
			karte5[i-2]=new Karta(karte[i]);
		}
		for(t=2;t<15;t++)
		{	karta1=null;
			parovi=new int[4][4];
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
			karte7[5]=new Karta(karta1);
			karte5[3]=new Karta(karta1);
			for(r=2;r<=t;r++)
			{	karta2=null;
				for(i=0;i<4;i++)
				{	karta=brojiboja(rednibroj(r,i));
					if(!karta.contains(Karte))
					{	karte2[i]=new Karta(karta);
						karta2=karta3;
						karta3=new Karta(karta);
					}
					else
						for(j=0;j<4;j++)
							parovi[j][i]=0;
				}
				if(karta2==null)
					continue;
				mnozilac=0;
				for(i=0;i<4;i++)
				{	if(maxboja==i&&boja[i]>=4)
						continue;
					for(j=0;j<3;j++)
					{	if(maxboja==j&&boja[j]>=4)
							continue;
						if(maxboja==i&&i==j&&boja[i]==3)
							continue;
						if(t==r&&i==j)
							continue;
						if(t==r&&j>i)
							break;
						mnozilac+=parovi[i][j];
					}
				}
				karte7[6]=new Karta(karta2);
				karte5[4]=new Karta(karta2);
				if(mnozilac>0)
					Pflop1(karte,karte7,karte5,mnozilac,rezultat,false);
				for(i=0;i<1;i++)
					if(boja[maxboja]>=3)
					{	if(karte1[maxboja]==null)
							break;
						if(karte2[maxboja]==null)
							break;
						karta1=brojiboja(rednibroj(t,maxboja));
						karta2=brojiboja(rednibroj(r,maxboja));
						if(karta1.equals(karta2))
							break;
						karte7[5]=new Karta(karta1);
						karte5[3]=new Karta(karta1);
						karte7[6]=new Karta(karta2);
						karte5[4]=new Karta(karta2);
						Pflop1(karte,karte7,karte5,1,rezultat,true);
					}
				for(i=0;i<1;i++)
					if(boja[maxboja]>=4)
					{	if(karte1[maxboja]==null)
							break;
						karta1=brojiboja(rednibroj(t,maxboja));
						mnozilac=0;
						for(j=0;j<4;j++)
						{	if(karte2[j]!=null)
							{	karta2=karte2[j];
								mnozilac++;
							}
						}
						if(mnozilac==0)
							break;
						karte7[5]=new Karta(karta1);
						karte5[3]=new Karta(karta1);
						karte7[6]=new Karta(karta2);
						karte5[4]=new Karta(karta2);
						Pflop1(karte,karte7,karte5,3,rezultat,true);
					}
				for(i=0;i<1;i++)
					if(boja[maxboja]>=4)
					{	if(karte2[maxboja]==null)
							break;
						karta2=brojiboja(rednibroj(t,maxboja));
						mnozilac=0;
						for(j=0;j<4;j++)
						{	if(karte1[j]!=null)
							{	karta1=karte1[j];
								mnozilac++;
							}
						}
						if(mnozilac==0)
							break;
						karte7[5]=new Karta(karta1);
						karte5[3]=new Karta(karta1);
						karte7[6]=new Karta(karta2);
						karte5[4]=new Karta(karta2);
						Pflop1(karte,karte7,karte5,3,rezultat,true);
					}
			}
		}
		double verovatnoca1=(rezultat[2]+(float)rezultat[1]/2)/(rezultat[0]+rezultat[1]+rezultat[2]);
		return (float)verovatnoca1;
	}
	
	public static void Pflop1(Karta karte[],Karta karte7[],Karta karte5[],int mnozilac,int rezultat[],boolean Boja)
	{	int parovi[][]=new int[52][52];//idu redom svi pikovi, pa ostali, od 2 do A zbog mod, radi lakšeg rada
		int rezultat1[],i,j,t,r,boja[]={0,0,0,0,0},maxboja=0;
		Karta ruka[],ruka7[],ruka5[];
		if(Boja)
			ruka7=rukaboja(karte7);
		else
			ruka7=rukabezboje(karte7);
		ruka5=ruka(karte5);
		//ručne karte koje učestvuju u formiranju ruke:
		Karta karta3=new Karta(),karta4=new Karta();
		int ind1=0,ind2=0;
		for(j=1;j<=5;j++)
			if(ruka7[j].equals(karte[0]))
			{	ind1=j;
				karta3=new Karta(karte[0]);
				break;
			}
		for(j=1;j<=5;j++)
			if(ruka7[j].equals(karte[1]))
			{	ind2=j;
				karta4=new Karta(karte[1]);
				break;
			}
		if((ind2<ind1||ind1==0)&&ind2>0)
		{	Karta p=karta4;
			karta4=karta3;
			karta3=p;
		}
		parovi=new int[52][52];
		for(Karta karta:karte7)
		{	int rednibroj=rednibroj(karta);
			parovi[rednibroj][rednibroj]=1;
			for(j=0;j<52;j++)
			{	parovi[j][rednibroj]=1;
				parovi[rednibroj][j]=1;
			}
		}
		rezultat1=new int[3];
		Poboljsavanje2karte1(ruka5,ruka7,karte5,parovi,karta3,karta4,rezultat1);
		rezultat1[2]=990-rezultat1[1]-rezultat1[0];
		for(j=0;j<3;j++)
			rezultat[j]+=rezultat1[j]*mnozilac;
	}
		
	public static float Pturn(int brojigraca, Karta karte[])
	{	//Karta t,r;//karte u ruci, flop, turn i river
		int rezultat[]=new int[3],rezultat1[]=new int[3],i,j;
		Karta ruka[],ruka7[],ruka5[],karte7[]=new Karta[7],karte5[]=new Karta[5];//7: svih 7 karata; 5: 5 karata na stolu
		ArrayList<int[]>[] lista;
		Arrays.sort(karte);
        ruka=ruka(karte);
        Main.time();
		int parovi[][]=new int[52][52];//idu redom svi pikovi, pa ostali, od 2 do A zbog mod, radi lakšeg rada
		//sa kojim kartama na turnu i riveru se poboljšava ruka i u koju:
    	lista=Poboljsavanje1karte(ruka,karte,parovi);
		ArrayList<Integer> spil=new ArrayList<>();
		for(i=0;i<52;i++)
			spil.add(i);
		for(i=0;i<5;i++)
			spil.remove((Integer)rednibroj(karte[i]));
		int l=spil.size();
		
		//za koje karte protivnika se remizira ili gubi:
		int k=7;
		for(k=8;k>=0;k--)
			for(int[] par:lista[k])
			for(int I=0;I<1;I++)
			{	for(j=0;j<3;j++)
				{	karte7[j]=karte[j+2];
					karte5[j]=karte[j+2];//5 karata na stolu, zajedno sa karta1 i karta2 daju karta 7.
				}
				for(j=0;j<2;j++)
				{	karte7[j+3]=brojiboja(par[j]);
					karte5[j+3]=brojiboja(par[j]);
				}
				for(j=0;j<2;j++)
					karte7[j+5]=karte[j];
				ruka7=ruka(karte7);
				ruka5=ruka(karte5);
				//ručne karte koje učestvuju u formiranju ruke:
				Karta karta1=new Karta(),karta2=new Karta();
				int ind1=0,ind2=0;
				for(j=1;j<=5;j++)
					if(ruka7[j].equals(karte[0]))
					{	ind1=j;
						karta1=new Karta(karte[0]);
						break;
					}
				for(j=1;j<=5;j++)
					if(ruka7[j].equals(karte[1]))
					{	ind2=j;
						karta2=new Karta(karte[1]);
						break;
					}
				if((ind2<ind1||ind1==0)&&ind2>0)
				{	Karta p=karta2;
					karta2=karta1;
					karta1=p;
				}
				parovi=new int[52][52];
				for(Karta karta:karte7)
				{	int rednibroj=rednibroj(karta);
					parovi[rednibroj][rednibroj]=1;
					for(j=0;j<52;j++)
					{	parovi[j][rednibroj]=1;
						parovi[rednibroj][j]=1;
					}
				}
				rezultat1=new int[3];
				Poboljsavanje2karte1(ruka5,ruka7,karte5,parovi,karta1,karta2,rezultat1);
				rezultat1[2]=990-rezultat1[1]-rezultat1[0];
				for(i=0;i<3;i++)
					rezultat[i]+=rezultat1[i];
			}
		double verovatnoca1=(rezultat[2]+(float)rezultat[1]/2)/(rezultat[0]+rezultat[1]+rezultat[2]);
		return (float)verovatnoca1;
	}
		
	public static float Priver(int brojigraca, Karta karte[])
	{	int pobeda=0,poraz=0,remi=0;
		Karta ruka[];
		int rezultat[]=new int[3];
		Spil spil=new Spil();
		ruka=ruka(karte);
		//rezultat=kombinacije(spil,ruka,brojigraca,K1,K2,F1,F2,F3,T,R);
		pobeda+=990-rezultat[1]-rezultat[2]+rezultat[0];
		remi+=rezultat[1];
		poraz+=rezultat[2];
		return (pobeda+(float)remi/2)/(pobeda+remi+poraz);
	}
    
    public static ArrayList<int[]>[] Poboljsavanje2karte(Karta ruka[],Karta karte[],int parovi[][])
    {
		ArrayList<int[]>[] lista = new ArrayList[9];
		Karta obojenekarte[]=new Karta[0],razlicitekarte[],spil[];
		int boja[] = new int[5];
		int maxboja,i,j,k;
		Karta[] uparenekarte,neuparenekarte,ostalekarte;
		for(i=0;i<9;i++)
			lista[i]=new ArrayList<>();
		for(Karta karta:karte)
		{	int rednibroj=rednibroj(karta);
			parovi[rednibroj][rednibroj]=1;
			for(i=0;i<52;i++)
			{	parovi[i][rednibroj]=1;
				parovi[rednibroj][i]=1;
			}
		}
		Arrays.sort(karte);
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
				j++;
		}
		razlicitekarte=new Karta[j];
		razlicitekarte[0]=karte[0];
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
			{	razlicitekarte[j]=karte[i];
				j++;
			}
		}
		spil=new Karta[13-razlicitekarte.length];
		prvapetlja:
		for(i=14,k=0;i>=2;i--)
		{	for(j=0;j<razlicitekarte.length;j++)
				if(razlicitekarte[j].id==i)
					continue prvapetlja;
			spil[k]=new Karta(0,i);
			k++;
		}
		for(Karta karta:karte)
			boja[karta.boja]++;
        for(maxboja=0;maxboja<4;maxboja++)
            if(boja[maxboja]>2)
                break;
        if(boja[maxboja]>2)
        {   obojenekarte=new Karta[boja[maxboja]];
			i=0;
			for(Karta karta:karte)
				if(karta.boja==maxboja)
				{   obojenekarte[i]=karta;
					i++;
				}
        }
		switch(ruka[0].id){
		case 6://full house
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[1];//ovde su uparene
			neuparenekarte[0]=ruka[4];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//poker: karta na triling:
			lista[7].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
		break;
		case 5://boja
			//samo kenta u boji:
			lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
		break;
		case 4:
		if(obojenekarte.length>2)//kenta
		{	//kenta u boji:
			lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//boja:
			lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
		}
		break;
		case 3://triling
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[2];
			neuparenekarte[0]=ruka[4];
			neuparenekarte[1]=ruka[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: karta na triling::
			lista[7].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//full house: karta na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,spil,3,parovi));
			//2 neke druge iste karte::
			lista[6].addAll(Dodavanje2karte(spil,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
		break;
		case 2://dva para
			uparenekarte=new Karta[2];
			uparenekarte[0]=ruka[1];
			uparenekarte[1]=ruka[3];
			neuparenekarte=new Karta[1];
			neuparenekarte[0]=ruka[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(uparenekarte,null,1,parovi));
			//full house: karta na par:
			lista[6].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//2 karte na preostalu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
		break;
		case 1://par
			uparenekarte=new Karta[1];
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[3];
			for(i=0;i<3;i++)
				neuparenekarte[i]=ruka[i+3];
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(uparenekarte,null,1,parovi));
			//full house: 1 karta na par i 1 karta na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(uparenekarte,neuparenekarte,3,parovi));
			//full house: 2 karte na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
			//triling: karta na par:
			lista[3].addAll(Dodavanje2karte(uparenekarte,spil,3,parovi));
			//2 para: karta na neuparenu kartu:
			lista[2].addAll(Dodavanje2karte(neuparenekarte,spil,3,parovi));
			//2 para: 2 neke druge iste karte::
			lista[2].addAll(Dodavanje2karte(spil,null,1,parovi));
		break;
		case 0://5 dasaka
			if(obojenekarte.length>2)
			{	//kenta u boji:
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
				//boja
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			}
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
			//triling: 2 karte na neuparenu kartu:
			lista[3].addAll(Dodavanje2karte(karte,null,1,parovi));
			//dva para: po 1 karta na 2 neuparene karte:
			lista[2].addAll(Dodavanje2karte(karte,null,2,parovi));
			//par: karta na neuparenu kartu:
			lista[1].addAll(Dodavanje2karte(karte,spil,3,parovi));
			//par: 2 neke druge iste karte::
			lista[1].addAll(Dodavanje2karte(spil,null,1,parovi));
		break;}
		return lista;
    }
    
    public static ArrayList<int[]>[] Poboljsavanje1karte(Karta ruka[],Karta karte[],int parovi[][])
    {
		ArrayList<int[]>[] lista = new ArrayList[9];
		Karta obojenekarte[]=new Karta[0],razlicitekarte[],spil[];
		int boja[] = new int[5];
		int maxboja,i,j,k;
		Karta[] uparenekarte,neuparenekarte,ostalekarte;
		for(i=0;i<9;i++)
			lista[i]=new ArrayList<>();
		for(Karta karta:karte)
		{	int rednibroj=rednibroj(karta);
			parovi[rednibroj][rednibroj]=1;
			for(i=0;i<52;i++)
			{	parovi[i][rednibroj]=1;
				parovi[rednibroj][i]=1;
			}
		}
		Arrays.sort(karte);
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
				j++;
		}
		razlicitekarte=new Karta[j];
		razlicitekarte[0]=karte[0];
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
			{	razlicitekarte[j]=karte[i];
				j++;
			}
		}
		spil=new Karta[13-razlicitekarte.length];
		prvapetlja:
		for(i=14,k=0;i>=2;i--)
		{	for(j=0;j<razlicitekarte.length;j++)
				if(razlicitekarte[j].id==i)
					continue prvapetlja;
			spil[k]=new Karta(0,i);
			k++;
		}
		for(Karta karta:karte)
			boja[karta.boja]++;
        for(maxboja=0;maxboja<4;maxboja++)
            if(boja[maxboja]>2)
                break;
        if(boja[maxboja]>2)
        {   obojenekarte=new Karta[boja[maxboja]];
			i=0;
			for(Karta karta:karte)
				if(karta.boja==maxboja)
				{   obojenekarte[i]=karta;
					i++;
				}
        }
		switch(ruka[0].id){
		case 6://full house
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[1];//ovde su uparene
			neuparenekarte[0]=ruka[4];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//poker: karta na triling:
			lista[7].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
		break;
		case 5://boja
			//samo kenta u boji:
			lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
		break;
		case 4:
		if(obojenekarte.length>2)//kenta
		{	//kenta u boji:
			lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//boja:
			lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
		}
		break;
		case 3://triling
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[2];
			neuparenekarte[0]=ruka[4];
			neuparenekarte[1]=ruka[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: karta na triling::
			lista[7].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//full house: karta na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,spil,3,parovi));
			//2 neke druge iste karte::
			lista[6].addAll(Dodavanje2karte(spil,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
		break;
		case 2://dva para
			uparenekarte=new Karta[2];
			uparenekarte[0]=ruka[1];
			uparenekarte[1]=ruka[3];
			neuparenekarte=new Karta[1];
			neuparenekarte[0]=ruka[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(uparenekarte,null,1,parovi));
			//full house: karta na par:
			lista[6].addAll(Dodavanje2karte(uparenekarte,ostalekarte,3,parovi));
			//2 karte na preostalu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
		break;
		case 1://par
			uparenekarte=new Karta[1];
			uparenekarte[0]=ruka[1];
			neuparenekarte=new Karta[3];
			for(i=0;i<3;i++)
				neuparenekarte[i]=ruka[i+3];
			//kenta u boji:
			if(obojenekarte.length>2)
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
			//poker: 2 karte na par:
			lista[7].addAll(Dodavanje2karte(uparenekarte,null,1,parovi));
			//full house: 1 karta na par i 1 karta na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(uparenekarte,neuparenekarte,3,parovi));
			//full house: 2 karte na neuparenu kartu:
			lista[6].addAll(Dodavanje2karte(neuparenekarte,null,1,parovi));
			//boja
			if(obojenekarte.length>2)
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
			//triling: karta na par:
			lista[3].addAll(Dodavanje2karte(uparenekarte,spil,3,parovi));
			//2 para: karta na neuparenu kartu:
			lista[2].addAll(Dodavanje2karte(neuparenekarte,spil,3,parovi));
			//2 para: 2 neke druge iste karte::
			lista[2].addAll(Dodavanje2karte(spil,null,1,parovi));
		break;
		case 0://5 dasaka
			if(obojenekarte.length>2)
			{	//kenta u boji:
				lista[8].addAll(Poboljsavanjeukentu(obojenekarte,maxboja,parovi,true));
				//boja
				lista[5].addAll(Poboljsavanjeuboju(obojenekarte,maxboja,parovi));
			}
			//kenta
			lista[4].addAll(Poboljsavanjeukentu(razlicitekarte,maxboja,parovi,false));
			//triling: 2 karte na neuparenu kartu:
			lista[3].addAll(Dodavanje2karte(karte,null,1,parovi));
			//dva para: po 1 karta na 2 neuparene karte:
			lista[2].addAll(Dodavanje2karte(karte,null,2,parovi));
			//par: karta na neuparenu kartu:
			lista[1].addAll(Dodavanje2karte(karte,spil,3,parovi));
			//par: 2 neke druge iste karte::
			lista[1].addAll(Dodavanje2karte(spil,null,1,parovi));
		break;}
		return lista;
    }
	
	public static ArrayList<int[]> Poboljsavanjeukentu(Karta Karte[],int maxboja,int parovi[][],boolean boja)
	{
		boolean as=false;
		ArrayList<int[]> lista = new ArrayList<>();
		int i,j,n=6;
		int niz1[]=new int[18];
		int niz2[]=new int[18];
		ArrayList<Integer>pocetak1=new ArrayList<>();
		ArrayList<Integer>pocetak2=new ArrayList<>();
		ArrayList<Integer>kraj1=new ArrayList<>();
		ArrayList<Integer>kraj2=new ArrayList<>();
		HashSet<Integer> rupe1=new HashSet<>();
		ArrayList<int[]> rupe2=new ArrayList<>();
		int karte[]=new int[Karte.length+1];
		for(i=0;i<Karte.length;i++)
			karte[i]=Karte[i].id;
		for(i=0;i<karte.length;i++)
			if(karte[i]==0)
			{	n=i;
				break;
			}
		if(karte[0]==14)
		{	karte[n]=1;
			n++;
			as=true;
		}
		//provera da li ima bar 3 karte u 5 uzastopnih brojeva za kentu:
		for(i=0;i<n-3;i++)
			switch (karte[i]-karte[i+3])
			{	case 3:
					pocetak1.add(karte[i+3]-1);
					pocetak1.add(karte[i+3]);
					kraj1.add(karte[i]+1);
					kraj1.add(karte[i]);
					break;
				case 4:
					pocetak1.add(karte[i+3]);
					kraj1.add(karte[i]);
					break;
			}
		for(i=0;i<n-2;i++)
			switch (karte[i]-karte[i+2])
			{	case 2:
					pocetak2.add(karte[i+2]-2);
					pocetak2.add(karte[i+2]-1);
					pocetak2.add(karte[i+2]);
					kraj2.add(karte[i]+2);
					kraj2.add(karte[i]+1);
					kraj2.add(karte[i]);
					break;
				case 3:
					pocetak2.add(karte[i+2]-1);
					pocetak2.add(karte[i+2]);
					kraj2.add(karte[i]+1);
					kraj2.add(karte[i]);
					break;
				case 4:
					pocetak2.add(karte[i+2]);
					kraj2.add(karte[i]);
					break;
			}
		//nizovi karata potrenih za kentu:
		for(Integer I:pocetak1)
			niz1[I+1]=1;
		for(Integer I:pocetak2)
			niz2[I+1]=1;
		for(Integer I:kraj1)
			niz1[I+1]=-1;
		for(Integer I:kraj2)
			niz2[I+1]=-1;
		for(i=1,j=0;i<18;i++)
		{	if(niz1[i]>0)
			{	niz1[i]+=j;
				j++;
			}
			else if(niz1[i]<0)
			{	niz1[i]+=j+1;
				j--;
			}
			else
				niz1[i]+=j;
		}
		for(i=0,j=0;i<18;i++)
		{	if(niz2[i]>0)
			{	niz2[i]+=j;
				j++;
			}
			else if(niz2[i]<0)
			{	niz2[i]+=j+1;
				j--;
			}
			else
				niz2[i]+=j;
		}
		for(i=0;i<n;i++)
		{	niz1[karte[i]+1]=0;
			niz2[karte[i]+1]=0;
		}
		for(i=0;i<18;i++)
			if(niz1[i]>0)
			{	rupe1.add(i-1);
				niz2[i]=0;
			}
		i=-1;j=0;
		while(j<18)
			if(niz2[j]==0)
				j++;
			else if(i==-1)
			{	i=j;j++;}
			else
			{	int par[]=new int[]{i-1,j-1};
				if(j-i<6&&i>=2&&j<=15)
					rupe2.add(par);
				niz2[j]-=niz2[i];
				niz2[i]=0;
				i=j;
				if(niz2[j]==0)
					i=-1;
				j++;
			}
		//popunjavanje liste parova karata:
		for(int I:rupe1)
		{	if(I<2||I>14)
				continue;
			int par1,par2;
			for(i=0;i<4;i++)
			{	if(boja)
					i=maxboja-1;
				par1=rednibroj(I,i);
				if(par1==-1)
					par1=12;
				if(parovi[par1][par1]==1)
				{	if(boja)
						break;
					continue;
				}
				parovi[par1][par1]=1;
				for(j=0;j<52;j++)
					if(parovi[par1][j]==0)
					{	parovi[par1][j]=1;
						parovi[j][par1]=1;
						par2=j;
						lista.add(new int[]{par1,par2});
					}
				if(boja)
					break;
			}
		}
		for(int par[]:rupe2)
		{	drugapetlja:
			for(i=0;i<4;i++)
				for(j=0;j<4;j++)
				{	int Par[]=new int[2];
					if(boja)
						i=j=maxboja-1;
					if(par[0]==1)
						par[0]=14;
					if(par[1]==1)
						par[1]=14;
					Par[0]=rednibroj(par[0],i);
					Par[1]=rednibroj(par[1],j);
					if(Par[0]>51||Par[1]>51)
						Par[0]+=0;
					if(parovi[Par[0]][Par[1]]==0)
					{	parovi[Par[0]][Par[1]]=1;
						parovi[Par[1]][Par[0]]=1;
						lista.add(Par);
					}
					if(boja)
						break drugapetlja;
				}
		}
		if(as)
			karte[karte.length-1]=0;
		return lista;
	}
	
	public static ArrayList<int[]> Poboljsavanjeuboju(Karta karte[],int maxboja,int parovi[][])
	{	
		ArrayList<int[]> lista = new ArrayList<>();
		int i,j;
		if(karte.length==4)
			for(i=2;i<15;i++)
			{	int par1,par2;
				par1=rednibroj(i,maxboja-1);
				parovi[par1][par1]=1;
				for(j=0;j<52;j++)
					if(parovi[par1][j]==0)
					{	parovi[par1][j]=1;
						parovi[j][par1]=1;
						par2=j;
						lista.add(new int[]{par1,par2});
					}
			}
		else if(karte.length==3)
			for(i=3;i<15;i++)
			{	int par1,par2;
				par1=rednibroj(i,maxboja-1);
				if(parovi[par1][par1]==1)
					continue;
				for(j=2;j<i;j++)
				{	par2=rednibroj(j,maxboja-1);
					if(parovi[par1][par2]==0)
					{	parovi[par1][par2]=1;
						parovi[par2][par1]=1;
						lista.add(new int[]{par1,par2});
					}
				}
			}
		return lista;
	}
	
	public static ArrayList<int[]> Dodavanje2karte(Karta karte1[],Karta karte2[],int ind,int parovi[][])
	{
		ArrayList<int[]> lista = new ArrayList<>();
		int i,j,rednibroj1,rednibroj2,boja1,boja2,n1,n2=0;
		n1=karte1.length;
		if(karte2!=null)
			n2=karte2.length;
		switch (ind)
		{	case 1://dodavanje 2 iste karte
				for(i=0;i<n1;i++)
					for(boja1=1;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]==1)
							continue;
						for(boja2=0;boja2<boja1;boja2++)//boja
						{	rednibroj2=rednibroj(karte1[i].id,boja2);
							lista.addAll(Dodavanje2karte1(rednibroj1,rednibroj2,parovi));
						}
					}
			break;
			case 2://dodavanje 2 karte iz istog niza
				for(i=1;i<n1;i++)
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]==1)
							continue;
						for(j=0;j<i;j++)
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte1[j].id,boja2);
								lista.addAll(Dodavanje2karte1(rednibroj1,rednibroj2,parovi));
							}
					}
				break;
			case 3://dodavanje po 1 karte iz oba niza
				for(i=0;i<n1;i++)
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]==1)
							continue;
						for(j=0;j<n2;j++)
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte2[j].id,boja2);
								lista.addAll(Dodavanje2karte1(rednibroj1,rednibroj2,parovi));
							}
					}
				break;
		}
		return lista;
	}
	
	public static ArrayList<int[]> Dodavanje2karte1(int rednibroj1,int rednibroj2,int parovi[][])
	{
		ArrayList<int[]> lista = new ArrayList<>();
		if(parovi[rednibroj2][rednibroj1]==1)
			return lista;
		if(parovi[rednibroj1][rednibroj2]==1)
			return lista;
		int[] par=new int[]{rednibroj1,rednibroj2};
		lista.add(par);
		parovi[rednibroj1][rednibroj2]=1;
		parovi[rednibroj2][rednibroj1]=1;
		return lista;
	}
    
    public static void Poboljsavanje2karte1(Karta ruka5[],Karta ruka7[],Karta karte1[],int parovi[][],Karta karta1,Karta karta2,int rezultat[])
    {
		Karta obojenekarte[]=new Karta[0], razlicitekarte[];
		int boja[] = new int[5];
		int maxboja,i,j,k;
		Karta[] uparenekarte,neuparenekarte,ostalekarte,spil;
		Karta karte[]=new Karta[karte1.length];
		for(i=0;i<karte1.length;i++)
			karte[i]=new Karta(karte1[i]);
		Arrays.sort(karte);
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
				j++;
		}
		razlicitekarte=new Karta[j];
		razlicitekarte[0]=karte[0];
		for(i=1,j=1;i<5;i++)
		{	if(karte[i].id!=karte[i-1].id)
			{	razlicitekarte[j]=karte[i];
				j++;
			}
		}
		spil=new Karta[13-razlicitekarte.length];
		prvapetlja:
		for(i=14,k=0;i>=2;i--)
		{	for(j=0;j<razlicitekarte.length;j++)
				if(razlicitekarte[j].id==i)
					continue prvapetlja;
			spil[k]=new Karta(0,i);
			k++;
		}
		for(Karta karta:karte)
			boja[karta.boja]++;
        for(maxboja=0;maxboja<4;maxboja++)
            if(boja[maxboja]>2)
                break;
        if(boja[maxboja]>2)
        {   obojenekarte=new Karta[boja[maxboja]];
			i=0;
			for(Karta karta:karte)
				if(karta.boja==maxboja)
				{   obojenekarte[i]=karta;
					i++;
				}
        }
		switch(ruka5[0].id)
		{case 8://kenta u boji
			//kenta u boji:
			Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);
			//2 neke druge karte:
			boolean remi=false;
			switch (Ruka.poredjenje(ruka7,ruka5))
			{	case 0:
					remi=true;
				case -1:
				for(i=1;i<52;i++)
					for(j=0;j<i;j++)
						Dodavanje2karte3(i,j,parovi,rezultat,remi);
			}
		break;
		case 7://poker
			uparenekarte=new Karta[1];//ovde je poker
			uparenekarte[0]=ruka5[1];
			neuparenekarte=new Karta[1];
			neuparenekarte[0]=ruka5[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			//poker: neke 2 karte:
			Dodavanje2karte4(ostalekarte,ostalekarte,3,parovi,ruka7[0].id-7,ruka5,ruka7,rezultat);
			
		break;
		case 6://full house
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka5[1];
			neuparenekarte=new Karta[1];//ovde su uparene
			neuparenekarte[0]=ruka5[4];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			//poker: karta na triling:
			Dodavanje2karte2(uparenekarte,ostalekarte,3,parovi,ruka7[0].id-7,karta1,karta2,rezultat);
			//full house/poker: 1 karta na par:
			Dodavanje2karte4(neuparenekarte,spil,3,parovi,ruka7[0].id-6,ruka5,ruka7,rezultat);
			//full house/poker: 2 neke druge iste karte:
			Dodavanje2karte4(ostalekarte,null,1,parovi,ruka7[0].id-6,ruka5,ruka7,rezultat);
			if(karta1.id!=karta2.id||karta1.id<ruka5[4].id)
			{	//full house: 2 neke druge različite karte:
				Dodavanje2karte2(ostalekarte,null,2,parovi,ruka7[0].id-6,new Karta(),new Karta(),rezultat);
			}
		break;
		case 5://boja
			ostalekarte=Stream.concat(Arrays.stream(karte), Arrays.stream(spil)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			//kenta u boji:
			Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);
			//boja: bar 1 obojena karta:
			Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			int poredjenje=Ruka.poredjenje(ruka7,ruka5);
			if(poredjenje==-1)
				poredjenje=-1;
			if(poredjenje==1)
				poredjenje=1;
			//boja: 2 iste karte:
			Dodavanje2karte2(ostalekarte,null,1,parovi,ruka7[0].id-5+poredjenje,karta1,karta2,rezultat);
			//boja: 2 različite karte:
			Dodavanje2karte2(ostalekarte,null,2,parovi,ruka7[0].id-5+poredjenje,karta1,karta2,rezultat);
		break;
		case 4://kenta
			ostalekarte=Stream.concat(Arrays.stream(karte), Arrays.stream(spil)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			if(obojenekarte.length>2)
			{	//kenta u boji:
				Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);
				//boja:
				Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			}
			//kenta: pojačavanje kente:
			Poboljsavanjeukentu1(razlicitekarte,maxboja,parovi,false,ruka7[0].id-4,ruka7[1],rezultat);
			//kenta: 2 iste karte:
			int a=ruka7[1].id-ruka5[1].id;
			if(a>0)
				a=15;
			else if(a<0)
				a=1;
			Dodavanje2karte2(ostalekarte,null,1,parovi,ruka7[0].id-4,new Karta(0,a),new Karta(),rezultat);
			//kenta: 2 različite karte:
			Dodavanje2karte2(ostalekarte,null,2,parovi,ruka7[0].id-4,new Karta(0,a),new Karta(),rezultat);
		break;
		case 3://triling
			uparenekarte=new Karta[1];//ovde je triling
			uparenekarte[0]=ruka5[1];
			neuparenekarte=new Karta[2];
			neuparenekarte[0]=ruka5[4];
			neuparenekarte[1]=ruka5[5];
			int ind=0;
			if(neuparenekarte[0].id>uparenekarte[0].id)
			{	ind++;
				if(neuparenekarte[1].id>uparenekarte[0].id)
					ind++;
			}
			Karta[] neuparenekarte1=new Karta[ind];//jače od trilinga
			Karta[] neuparenekarte2=new Karta[2-ind];//slabije od trilinga
			for(i=0;i<ind;i++)
				neuparenekarte1[i]=neuparenekarte[i];
			for(;i<2;i++)
				neuparenekarte2[i-ind]=neuparenekarte[i];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			//kenta u boji:
			if(obojenekarte.length>2)
				Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);

			//poker: karta na triling::
			Dodavanje2karte2(uparenekarte,ostalekarte,3,parovi,ruka7[0].id-7,karta1,karta2,rezultat);
			//full house: 2 karte na neuparenu kartu:
			if((neuparenekarte1.length>0&&karta1.id==karta2.id&&karta2.id==neuparenekarte1[0].id)||
			   (neuparenekarte1.length>1&&karta1.id==karta2.id&&karta2.id==neuparenekarte1[1].id))
			{	
				Dodavanje2karte2(neuparenekarte1,null,1,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
				return;
			}
			else
				Dodavanje2karte2(neuparenekarte1,null,1,parovi,ruka7[0].id-6,new Karta(0,1),new Karta(0,1),rezultat);
			Dodavanje2karte2(neuparenekarte2,null,1,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//full house: 2 karte na 2 neuparene karte:
			Dodavanje2karte2(neuparenekarte,null,2,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//full house: karta na neuparenu kartu:
			Dodavanje2karte2(neuparenekarte,spil,3,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//full house: 2 neke druge iste karte:
			Dodavanje2karte2(spil,null,1,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//boja
			if(obojenekarte.length>2)
				Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			//kenta
			Poboljsavanjeukentu1(razlicitekarte,maxboja,parovi,false,ruka7[0].id-4,ruka7[1],rezultat);
			//triling: 2 neke druge različite karte:
			Dodavanje2karte4(spil,null,2,parovi,ruka7[0].id-3,ruka5,ruka7,rezultat);
		break;
		case 2://dva para
			uparenekarte=new Karta[2];
			uparenekarte[0]=ruka5[1];
			uparenekarte[1]=ruka5[3];
			neuparenekarte=new Karta[1];
			neuparenekarte[0]=ruka5[5];
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			ostalekarte=Stream.concat(Arrays.stream(uparenekarte), Arrays.stream(ostalekarte)).toArray(Karta[]::new);
			Arrays.sort(ostalekarte);
			//kenta u boji:
			if(obojenekarte.length>2)
				Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);
			//poker: 2 karte na par:
			Dodavanje2karte2(uparenekarte,null,1,parovi,ruka7[0].id-7,karta1,karta2,rezultat);
			//full house: 1 karta na par:
			Dodavanje2karte4(uparenekarte,ostalekarte,3,parovi,ruka7[0].id-6,ruka5,ruka7,rezultat);
			//full house: 2 karte na preostalu kartu:
			Dodavanje2karte2(neuparenekarte,null,1,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//boja:
			if(obojenekarte.length>2)
				Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			//kenta:
			Poboljsavanjeukentu1(razlicitekarte,maxboja,parovi,false,ruka7[0].id-4,ruka7[1],rezultat);
			ostalekarte=Stream.concat(Arrays.stream(neuparenekarte), Arrays.stream(spil)).toArray(Karta[]::new);
			//2 para: 1 karta na preostalu kartu:
			Dodavanje2karte4(neuparenekarte,spil,3,parovi,ruka7[0].id-2,ruka5,ruka7,rezultat);
			//2 para: 2 neke druge iste karte:
			Dodavanje2karte4(ostalekarte,null,1,parovi,ruka7[0].id-2,ruka5,ruka7,rezultat);
			//2 neke druge različite karte:
			Dodavanje2karte4(spil,null,2,parovi,ruka7[0].id-2,ruka5,ruka7,rezultat);
		break;
		case 1://par
			uparenekarte=new Karta[1];
			uparenekarte[0]=ruka5[1];
			neuparenekarte=new Karta[3];
			for(i=0;i<3;i++)
				neuparenekarte[i]=ruka5[i+3];
			//kenta u boji:
			if(obojenekarte.length>2)
				Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);

			//poker: 2 karte na par:
			Dodavanje2karte2(uparenekarte,null,1,parovi,ruka7[0].id-7,karta1,karta2,rezultat);
			//full house: 1 karta na par i 1 karta na neuparenu kartu:
			Dodavanje2karte2(uparenekarte,neuparenekarte,3,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//full house: 2 karte na neuparenu kartu:
			Dodavanje2karte2(neuparenekarte,null,1,parovi,ruka7[0].id-6,karta1,karta2,rezultat);
			//boja
			if(obojenekarte.length>2)
				Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			//kenta
			Poboljsavanjeukentu1(razlicitekarte,maxboja,parovi,false,ruka7[0].id-4,ruka7[1],rezultat);
			//triling: karta na par:
			Dodavanje2karte2(uparenekarte,spil,3,parovi,ruka7[0].id-3,karta1,karta2,rezultat);
			//3 para: po 1 karta na 2 neuparene karte:
			Dodavanje2karte4(neuparenekarte,null,2,parovi,ruka7[0].id-2,ruka5,ruka7,rezultat);
			//2 para: karta na neuparenu kartu:
			Dodavanje2karte4(neuparenekarte,spil,3,parovi,ruka7[0].id-2,ruka5,ruka7,rezultat);
			//2 para: 2 neke druge iste karte:
			Dodavanje2karte2(spil,null,1,parovi,ruka7[0].id-2,karta1,karta2,rezultat);
			//par: 2 neke druge različite karte:
			Dodavanje2karte2(spil,null,2,parovi,ruka7[0].id-1,karta1,karta2,rezultat);
		break;
		case 0://5 dasaka
			if(obojenekarte.length>2)
			{	//kenta u boji:
				Poboljsavanjeukentu1(obojenekarte,maxboja,parovi,true,ruka7[0].id-8,ruka7[1],rezultat);
				//boja
				Poboljsavanjeuboju1(obojenekarte,maxboja,parovi,ruka7[0].id-5,karta1,karta2,rezultat);
			}
			//kenta
			Poboljsavanjeukentu1(razlicitekarte,maxboja,parovi,false,ruka7[0].id-4,ruka7[1],rezultat);
			//triling: 2 karte na neuparenu kartu:
			Dodavanje2karte2(karte,null,1,parovi,ruka7[0].id-3,karta1,karta2,rezultat);
			//dva para: po 1 karta na 2 neuparene karte:
			Dodavanje2karte2(karte,null,2,parovi,ruka7[0].id-2,karta1,karta2,rezultat);
			//par: karta na neuparenu kartu:
			Dodavanje2karte2(karte,spil,3,parovi,ruka7[0].id-1,karta1,karta2,rezultat);
			//par: 2 neke druge iste karte::
			Dodavanje2karte2(spil,null,1,parovi,ruka7[0].id-1,karta1,karta2,rezultat);
			//5 dasaka: 2 neke druge različite karte:
			Dodavanje2karte2(spil,null,2,parovi,ruka7[0].id-0,karta1,karta2,rezultat);
		break;}
    }
	
	public static void Poboljsavanjeukentu1(Karta Karte[],int maxboja,int parovi[][],boolean boja,int rukarazlika,Karta karta1,int rezultat[])
	{
		if(rukarazlika>0)
			return;
		else if(rukarazlika<0)
			karta1=new Karta();
		boolean as=false;
		int i,j,n=6,k,a;
		int Niz1[]=new int[11],Niz2[]=new int[11];
		ArrayList<int[]>niz1=new ArrayList<>();
		ArrayList<int[]>niz2=new ArrayList<>();
		ArrayList<int[]> rupe1=new ArrayList<>();
		ArrayList<int[]> rupe2=new ArrayList<>();
		int karte[]=new int[Karte.length+1];
		for(i=0;i<Karte.length;i++)
			karte[i]=Karte[i].id;
		for(i=0;i<karte.length;i++)
			if(karte[i]==0)
			{	n=i;
				break;
			}
		if(karte[0]==14)
		{	karte[n]=1;
			n++;
			as=true;
		}
		//provera da li ima bar 3 karte u 5 uzastopnih brojeva za kentu:
		for(i=0;i<n-3;i++)
			switch (karte[i]-karte[i+3])
			{	case 3:
					k=karte[i+3];
					if(k-1<=10&&k-1>=1&&Niz1[k-1]==0)
					{	a=k+3;
						if(karte[0]==k+4)
							a++;
						if(karte[0]!=k+3||karte[4]!=k-1)
							niz1.add(new int[]{a,k-1,k,k+1,k+2,k+3});
						Niz1[k-1]=1;
					}
				case 4:
					k=karte[i+3];
					if(k<=10&&k>=1&&Niz1[k]==0)
					{	a=k+4;
						if(karte[0]==k+5)
							a++;
						if(karte[0]!=k+4||karte[4]!=k)
							niz1.add(new int[]{a,k,k+1,k+2,k+3,k+4});
						Niz1[k]=1;
					}
					break;
			}
		for(i=0;i<n-2;i++)
			switch (karte[i]-karte[i+2])
			{	case 2:
					k=karte[i+2];
					if(k-2<=10&&k-2>=1&&Niz2[k-2]==0)
					{	a=k+2;
						if(karte[0]==k+3)
							a++;
						if(karte[0]==k+4)
							a+=2;
						niz2.add(new int[]{k+2,k-2,k-1,k,k+1,k+2});
						Niz2[k-2]=1;
					}
				case 3:
					k=karte[i+2];
					if(k-1<=10&&k-1>=1&&Niz2[k-1]==0)
					{	a=k+3;
						if(karte[0]==k+4)
							a++;
						if(karte[0]==k+5)
							a+=2;
						niz2.add(new int[]{k+3,k-1,k,k+1,k+2,k+3});
						Niz2[k-1]=1;
					}
				case 4:
					k=karte[i+2];
					if(k<=10&&k>=1&&Niz2[k]==0)
					{	a=k+4;
						if(karte[0]==k+5)
							a++;
						if(karte[0]==k+6)
							a+=2;
						niz2.add(new int[]{k+4,k,k+1,k+2,k+3,k+4});
						Niz2[k]=1;
					}
					break;
			}
		//nizovi karata potrenih za kentu:
		for(int niz[]:niz1)
		{	i=karte.length-1;
			for(int b=1;b<niz.length;b++)
			{	while(i>=0&&niz[b]>karte[i])
					i--;
				if(i<0||niz[b]!=karte[i])
				{	rupe1.add(new int[]{niz[b],niz[0]});
					break;
				}
				else
					i--;
			}
		}
		for(int niz[]:niz2)
		{	int c[]=new int[3];
			j=0;
			i=karte.length-1;
			for(int b=1;b<niz.length;b++)
			{	//try{
				while(i>=0&&niz[b]>karte[i])
					i--;
				/*}catch(Exception e){
					i-=0;
				}*/
				if(i<0||niz[b]!=karte[i])
				{	c[j]=niz[b];
					j++;
				}
				else
					i--;
				if(j==2)
				{	c[2]=niz[0];
					rupe2.add(c);
					break;
				}
			}
		}
		//popunjavanje liste parova karata:
		for(int par[]:rupe2)
		{	int rednibroj[]=new int[2];
			if(par[0]==1)
				par[0]=14;
			if(par[1]==1)
				par[1]=14;
			rednibroj[0]=rednibroj(par[0],maxboja-1);
			rednibroj[1]=rednibroj(par[1],maxboja-1);
			if(rukarazlika==0&&par[2]<karta1.id)
				continue;
			drugapetlja:
			for(i=0;i<4;i++)
				for(j=0;j<4;j++)
				{	if(boja)
						i=j=maxboja-1;
					rednibroj[0]=rednibroj(par[0],i);
					rednibroj[1]=rednibroj(par[1],j);
					boolean uslov=rukarazlika==0&&par[2]==karta1.id;
					Dodavanje2karte3(rednibroj[0],rednibroj[1],parovi,rezultat,uslov);
					if(boja)
						break drugapetlja;
				}
		}
		for(i=0;i<rupe1.size()-1;i++)
		{	int max=rupe1.get(i)[0],indmax=0;
			for(j=1;j<rupe1.size();j++)
			{	if(rupe1.get(j)[0]>max)
				{	max=rupe1.get(j)[0];
					indmax=j;
				}
				if(max>rupe1.get(i)[0])
				{	int[] p=rupe1.get(indmax);
					rupe1.set(indmax,rupe1.get(i));
					rupe1.set(i,p);
				}
			}
		}
		for(int[] par:rupe1)
		{	if(par[0]<1||par[0]>14)
				continue;
			if(par[0]==1)
				par[0]=14;
			int rednibroj1,rednibroj2;
			for(i=0;i<4;i++)
			{	if(boja)
					i=maxboja-1;
				rednibroj1=rednibroj(par[0],i);
				if(parovi[rednibroj1][rednibroj1]>0)
				{	if(boja)
						break;
					continue;
				}
				parovi[rednibroj1][rednibroj1]=1;
				if(rukarazlika==0&&par[1]<karta1.id)
				{	if(boja)
						break;
					continue;
				}
				parovi[rednibroj1][rednibroj1]=1;
				for(j=0;j<52;j++)
				{	rednibroj2=j;
					boolean uslov=rukarazlika==0&&par[1]==karta1.id;
					Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
				}
				if(boja)
					break;
			}
		}
		if(as)
			karte[karte.length-1]=0;
	}
	
	public static void Poboljsavanjeuboju1(Karta karte[],int maxboja,int parovi[][],int rukarazlika,Karta karta1,Karta karta2,int rezultat[])
	{	
		if(rukarazlika>0)
			return;
		if(rukarazlika<0)
		{	karta1=new Karta();
			karta2=new Karta();
		}
		int i,j;
		if(karte.length>=3)
			for(i=3;i<15;i++)
			{	int rednibroj1,rednibroj2;
				Karta k1,k2;
				rednibroj1=rednibroj(i,maxboja-1);
				k1=brojiboja(rednibroj1);
				if(parovi[rednibroj1][rednibroj1]>0)
					continue;
				if(rukarazlika==0&&k1.id<karta1.id)
					continue;
				for(j=2;j<i;j++)
				{	rednibroj2=rednibroj(j,maxboja-1);
					k2=brojiboja(rednibroj2);
					boolean uslov=rukarazlika==0&&karte.length==5&&Math.max(karta1.id,Math.max(k1.id,k2.id))<karte[4].id;
					Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
				}
			}
		if(karte.length>=4)
			for(i=2;i<15;i++)
			{	int rednibroj1,rednibroj2;
				Karta k1,k2;
				rednibroj1=rednibroj(i,maxboja-1);
				if(parovi[rednibroj1][rednibroj1]>0)
					continue;
				parovi[rednibroj1][rednibroj1]=1;
				k1=brojiboja(rednibroj1);
				if(rukarazlika==0&&k1.id<karta1.id)
					continue;
				for(int k=0;k<4;k++)
				{	if(k==maxboja-1)
						continue;
					for(j=2;j<15;j++)
					{	rednibroj2=rednibroj(j,k);
						k2=brojiboja(rednibroj1);
						boolean uslov=rukarazlika==0&&karte.length==5&&Math.max(karta1.id,Math.max(k1.id,k2.id))<karte[4].id;
						Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
					}
				}
			}
	}
	
	public static void Dodavanje2karte2(Karta karte1[],Karta karte2[],int ind,int parovi[][],int rukarazlika,Karta karta1,Karta karta2,int rezultat[])
	{
		if(rukarazlika>0)
			return;
		if(rukarazlika<0)
		{	karta1=new Karta();
			karta2=new Karta();
		}
		int i,j,rednibroj1,rednibroj2,boja1,boja2,n1,n2=0;
		n1=karte1.length;
		if(karte2!=null)
			n2=karte2.length;
		switch(ind)
		{	case 1://dodavanje 2 iste karte
				for(i=0;i<n1;i++)
				{	try{
					if(rukarazlika==0&&karte1[i].id<karta1.id)
						continue;
					}catch(Exception e){
					i+=0;
					}
					for(boja1=1;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						boolean uslov=rukarazlika==0&&(karte1[i].id==karta1.id||karta1.id==0);
						for(boja2=0;boja2<boja1;boja2++)//boja
						{	rednibroj2=rednibroj(karte1[i].id,boja2);
							Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
						}
					}
				}
			break;
			case 2://dodavanje 2 karte iz istog niza
				for(i=0;i<n1-1;i++)
				{	if(rukarazlika==0&&karte1[i].id<karta1.id)
						continue;
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						for(j=i+1;j<n1;j++)
						{	if(rukarazlika==0&&karte1[i].id==karta1.id&&karte1[j].id<karta2.id)
								continue;
							boolean uslov=rukarazlika==0&&(karta1.id==0||(karte1[i].id==karta1.id&&karta2.id==0)||(karte1[i].id==karta1.id&&karte1[j].id==karta2.id));
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte1[j].id,boja2);
								Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
							}
						}
					}
				}
			break;
			case 3://dodavanje po 1 karte iz oba niza
				for(i=0;i<n1;i++)
				{	if(rukarazlika==0&&karte1[i].id<karta1.id)
						continue;
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						for(j=0;j<n2;j++)
						{	if(rukarazlika==0&&karte1[i].id==karta1.id&&karte2[j].id<karta2.id)
								continue;
							boolean uslov=rukarazlika==0&&(karta1.id==0||(karte1[i].id==karta1.id&&karta2.id==0)||(karte1[i].id==karta1.id&&karte2[j].id==karta2.id));
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte2[j].id,boja2);
								Dodavanje2karte3(rednibroj1,rednibroj2,parovi,rezultat,uslov);
							}
						}
					}
				}
			break;
		}
	}
	
	public static void Dodavanje2karte3(int rednibroj1,int rednibroj2,int parovi[][],int rezultat[],boolean remi)
	{	if(parovi[rednibroj2][rednibroj1]>0)
			return;
		if(parovi[rednibroj1][rednibroj2]>0)
			return;
		if(rednibroj1==rednibroj2)
			return;
		int i,rez;
		if(remi)
		{	rezultat[1]++;
			parovi[rednibroj1][rednibroj2]=1;
			parovi[rednibroj2][rednibroj1]=1;
			rez=0;
		}
		else
		{	rezultat[0]++;
			parovi[rednibroj1][rednibroj2]=2;
			parovi[rednibroj2][rednibroj1]=2;
			rez=-1;
		}
	}
	
	public static void Dodavanje2karte4(Karta karte1[],Karta karte2[],int ind,int parovi[][],int rukarazlika,Karta[] ruka5,Karta[] ruka7,int rezultat[])
	{	if(rukarazlika>0)
			return;
		int i,j,rednibroj1,rednibroj2,boja1,boja2,n1,n2=0;
		n1=karte1.length;
		if(karte2!=null)
			n2=karte2.length;
		Karta k1,k2;
		switch(ind)
		{	case 1://dodavanje 2 iste karte
				for(i=0;i<n1;i++)
					for(boja1=1;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						for(boja2=0;boja2<boja1;boja2++)//boja
						{	rednibroj2=rednibroj(karte1[i].id,boja2);
							Dodavanje2karte5(rednibroj1,rednibroj2,ruka5,ruka7,parovi,rezultat);
						}
					}
			break;
			case 2://dodavanje 2 karte iz istog niza
				for(i=0;i<n1-1;i++)
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						for(j=i+1;j<n1;j++)
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte1[j].id,boja2);
								Dodavanje2karte5(rednibroj1,rednibroj2,ruka5,ruka7,parovi,rezultat);
							}
					}
			break;
			case 3://dodavanje po 1 karte iz oba niza
				for(i=0;i<n1;i++)
					for(boja1=0;boja1<4;boja1++)//boja
					{	rednibroj1=rednibroj(karte1[i].id,boja1);
						if(parovi[rednibroj1][rednibroj1]>0)
							continue;
						for(j=0;j<n2;j++)
							for(boja2=0;boja2<4;boja2++)//boja
							{	rednibroj2=rednibroj(karte2[j].id,boja2);
								Dodavanje2karte5(rednibroj1,rednibroj2,ruka5,ruka7,parovi,rezultat);
							}
					}
			break;
		}
	}
	
	public static void Dodavanje2karte5(int rednibroj1,int rednibroj2,Karta[] ruka5,Karta[] ruka7,int parovi[][],int rezultat[])
	{	Karta k1=brojiboja(rednibroj1);
		Karta k2=brojiboja(rednibroj2);
		if(parovi[rednibroj1][rednibroj2]>0)
			return;
		if(parovi[rednibroj2][rednibroj1]>0)
			return;
		if(rednibroj1==rednibroj2)
			return;
		Karta ruka[];
		Karta kartee[]=new Karta[7];
		for(int I=0;I<5;I++)
			kartee[I]=(new Karta(ruka5[I+1].boja,ruka5[I+1].id));
		kartee[5]=k1;
		kartee[6]=k2;
		ruka=ruka(kartee);
		int rez=Ruka.poredjenje(ruka7,ruka);
		rezultat[rez+1]++;
		parovi[rednibroj1][rednibroj2]=1-rez;
		parovi[rednibroj2][rednibroj1]=1-rez;
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
			int l=obojenekarte.size();
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
			int l=obojenekarte.size();
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