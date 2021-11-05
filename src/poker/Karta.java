package poker;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fpesic
 */
public class Karta implements Comparable<Karta>
{	
	public int boja;
	public char broj;
	public int id;
    //0x2660 pik
    //0x2663 tref
    //0x2666 karo
    //0x2665 herc
	
	public Karta()
	{	broj=0;
		boja=0;
		id=0;
	}
	
	public Karta(int boja,int id)
    {   this.boja=boja;
        this.id=id;
        if(id>=2&&id<=10)
            this.broj=(char)id;
        else if(id==11)
            this.broj='J';
        else if(id==12)
            this.broj='Q';
        else if(id==13)
            this.broj='K';
        else if(id==14)
            this.broj='A';
    }
	
	public Karta(Karta karta)
    {   this.boja=karta.boja;
        this.id=karta.id;
		this.broj=karta.broj;
    }
	
    public boolean equals(Karta k)
	{	if(this.boja==k.boja)
			if(this.id==k.id)
				return true;
		return false;
	}
	
	public boolean contains(ArrayList<Karta> karte)
	{	for(Karta karta:karte)
			if(this.equals(karta))
				return true;
		return false;
	}
	
	@Override
	public String toString()
	{	char broj1[]=new char[3];
		if(id<10)
		{	broj1[0]=(char)((char)id+48);
			broj1[1]=' ';
			broj1[2]=' ';
		}
		else if(id==10)
		{	broj1[0]='1';
			broj1[1]='0';
			broj1[2]=' ';
		}
		else
		{	broj1[0]=broj;
			broj1[1]=' ';
			broj1[2]=' ';
		}
        String S;
		if(boja==1)
			S=String.format("%s%c%c%c","pik  ",broj1[0],broj1[1],broj1[2]);
		else if(boja==2)
			S=String.format("%s%c%c%c","tref ",broj1[0],broj1[1],broj1[2]);
		else if(boja==3)
			S=String.format("%s%c%c%c","karo ",broj1[0],broj1[1],broj1[2]);
		else
			S=String.format("%s%c%c%c","herc ",broj1[0],broj1[1],broj1[2]);
		/*if(boja==1)
			S=String.format("%c%s",0x2660,broj1);
		else if(boja==2)
			S=String.format("%c%s",0x2663,broj1);
		else if(boja==3)
			S=String.format("%c%s",0x2666,broj1);
		else
			S=String.format("%c%s",0x2665,broj1);*/
		return S;
	}

	public int compare(Karta K1, Karta K2) {
		if(K1.id>K2.id)
			return -1;
		if(K2.id>K1.id)
			return 1;
		return 0;
	}

	@Override
	public int compareTo(Karta K) {
		if(id>K.id)
			return -1;
		if(K.id>id)
			return 1;
		return 0;
	}
	
	public static String dvekarte[]={"AA","KK","QQ","JJ","1010","99","77","55","AKb","AQb","AJb","A10b","A9b","A7b","A5b",
		"KQb","KJb","K10b","K9b","K7b","K5b","QJb","Q10b","Q9b","Q7b","Q5b",
		"AK","AQ","AJ","A10","A9","A7","A5","KQ","KJ","K10","K9","K7","K5","QJ","Q10","Q9","Q7","Q5",
		"J-1b","J-2b","J-3b","Jb","9-1b","9-2b","9-3b","9b",
		"J-1","J-2","J-3","J","9-1","9-2","9-3","9"};//J i 10
	public static int dvekarte(Karta karta1,Karta karta2)
	{	String k1="",k2="",karte;
		if(karta2.id>karta1.id)
		{	Karta p=karta1;
			karta1=karta2;
			karta2=p;
		}
		if(karta1.id==karta2.id)
		{	if(karta1.id>=11)
				k1+=karta1.broj;
			else if(karta1.id==10)
				k1+="10";
			else if(karta1.id==9||karta1.id==8)
				k1+="9";
			else if(karta1.id==7||karta1.id==6)
				k1+="7";
			else
				k1+="5";
			k2+=k1;
		}
		else if(karta1.id>=12)
		{	k1+=karta1.broj;
			if(karta2.id>=11)
				k2+=(karta2.broj);
			else if(karta2.id==10)
				k1+="10";
			else if(karta2.id==9||karta2.id==8)
				k2+="9";
			else if(karta2.id==7||karta2.id==6)
				k2+="7";
			else
				k2+="5";
		}
		else 
		{	if(karta1.id>=10)
				k1+="J";
			else
				k1+="9";
			if(karta1.id-karta2.id<=3)
				k2=k2+'-'+String.valueOf(karta1.id-karta2.id);
		}
		karte=k1+k2;
		if(karta1.boja==karta2.boja)
			karte+='b';
		int index=0;
		for(String s:dvekarte)
			if(karte.equals(s))
				break;
			else
				index++;
		return index;
	}
}
