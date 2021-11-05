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
public class Spil
{	
	public List<Karta> spil;
	public Spil()
	{	spil=new ArrayList<>();
		for(int i=14;i>=2;i--)
		{   Karta karta=new Karta(1,i);
			spil.add(karta);
			karta=new Karta(2,i);
			spil.add(karta);
			karta=new Karta(3,i);
			spil.add(karta);
			karta=new Karta(4,i);
			spil.add(karta);
		}
	}
	
	public void shuffle()
	{	Collections.shuffle(spil);
	}
	
	@Override
	public String toString()
	{	int j=1;
		String string="";
		for(int i=0;i<52;i++,j++)
		{	//stream<<setw(3)<<spil.spil.get(i);
			string+=String.format("%3d",spil.get(i));
			if(j==4)
			{	string+='\n';
				//stream<<endl;
				j=0;
			}
		}
		return string;
	}

	int indexOf(Karta K)
	{	int index=0;
		for(Karta karta:spil)
		{	if(karta.equals(K))
				return index;
			index++;
		}
		return -1;
	}
	
	{/*boolean contains(Karta K)
	{	int index=0;
		for(Karta karta:spil)
		{	if(karta.equals(K))
				return true;
			index++;
		}
		return false;
	}*/}
}