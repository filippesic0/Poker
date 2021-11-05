package poker;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fpesic
 */
public class Istorija
{	
	public float[] verovatnoca;
	public int[] brojbetova,brojigraca,brojigraca1,pozicija,novacpot;
	public float nagrada;
	int akcija;
	
	public static final double[] novackrozppot=
		{0, 1/21.0, 1/13.0, 1/8.0, 1/5.0, 1/3.0, 1/2.0, 1/1.5,	1, 1.5, 2, 3, 5, 8, 13, 21};
	
	public Istorija()
	{	verovatnoca=new float[4];
		for(int i=0;i<3;i++)
			verovatnoca[i]=-10;
		brojbetova=new int[4];//koliko igrača je podiglo ulog bet, raise, raise,...
		brojigraca=new int[4];//broj igrača za stolom
		brojigraca1=new int[4];//broj igrača sa kartama i param u ruci
		pozicija=new int[4];//broj igrača koji su na redu posle nas
		novacpot=new int[4];//odnos preostalog novca kod igrača i ukupnog uloga
		akcija=-10;
	}

	public void parametripreflop(Igrac naredu,int brojbetova1)
	{	if(brojbetova1>2)
			brojbetova1=2;
		brojbetova[3]=brojbetova1;
		switch(Math.round(Ruka.ocekivanje(naredu)))
		{	case 2:
				brojigraca[3]=0;
			break;
			case 3:
				brojigraca[3]=1;
			break;
			case 4:case 5:
				brojigraca[3]=2;
			break;
			case 6:case 7:
				brojigraca[3]=3;
			break;
			default:
				brojigraca[3]=4;
		}
		switch(Math.round(Ruka.ocekivanje1(naredu)))
		{	case 2:
				brojigraca1[3]=0;
			break;
			case 3:
				brojigraca1[3]=1;
			break;
			case 4:case 5:
				brojigraca1[3]=2;
			break;
			default:
				brojigraca1[3]=3;
		}
		switch(Math.round(Ruka.pozicija(naredu,2)))
		{	case 0:case 1:
				pozicija[3]=0;
			break;
			case 2:case 3:
				pozicija[3]=1;
			break;
			case 4:case 5:case 6:
				pozicija[3]=2;
			break;
			default:
				pozicija[3]=3;
		}
		if(Math.round(Ruka.pozicija(naredu,2))==-1)
		{	System.out.print("Istorija linija 82");
			System.exit(0);
			//brojbetova1+=0;
		}
	}

	public void parametriflopturnriver(Igrac naredu,int flop,int brojbetova1,int ukupanulog)
	{	if(brojbetova1>2)
			brojbetova1=2;
		brojbetova[flop]=brojbetova1;
		switch(Math.round(Ruka.ocekivanje(naredu)))
		{	case 2:
				brojigraca[flop]=0;
			break;
			case 3:
				brojigraca[flop]=1;
			break;
			case 4:case 5:
				brojigraca[flop]=2;
			break;
			case 6:case 7:
				brojigraca[flop]=3;
			break;
			default:
				brojigraca[flop]=4;
		}
		switch(Math.round(Ruka.ocekivanje1(naredu)))
		{	case 2:
				brojigraca1[flop]=0;
			break;
			case 3:
				brojigraca1[flop]=1;
			break;
			case 4:case 5:
				brojigraca1[flop]=2;
			break;
			default:
				brojigraca1[flop]=3;
		}
		switch(Math.round(Ruka.pozicija(naredu,0)))
		{	case 0:case -1:
				pozicija[flop]=0;
			break;
			case 1:
				pozicija[flop]=1;
			break;
			case 2:case 3:
				pozicija[flop]=2;
			break;
			case 4:case 5:
				pozicija[flop]=3;
			break;
			default:
				pozicija[flop]=4;
		}
		float np=(float)naredu.novac/ukupanulog;
		for(int i=0;i<novackrozppot.length;i++)
		{	double d=novackrozppot[i];
			if(np<d)
			{	novacpot[flop]=i;
				break;
			}
		}
	}
	
	@Override
	
	public String toString()
	{	
	/*	if(verturn+10<0.0001)
			return "\t"+verflop+'\t'+ulogflop+'\t'+novacflop+'\t'+brojigraca+'\t'+akcija+'\t'+nagrada;
		else if(verriver+10<0.0001)
			return "\t"+verturn+'\t'+ulogturn+'\t'+novacturn+'\t'+brojigraca+'\t'+akcija+'\t'+nagrada;
		return "\t"+verriver+'\t'+ulogriver+'\t'+novacriver+'\t'+brojigraca+'\t'+akcija+'\t'+nagrada;
	*/	
		System.out.print("Istorija linija 157");
		System.exit(0);
		return "";
	}
}
