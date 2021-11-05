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
	public int[] brojbetova,brojigraca,brojigraca1,pozicija;
	public float nagrada;
	int akcija;
	
	public Istorija()
	{	verovatnoca=new float[4];
		for(int i=0;i<3;i++)
			verovatnoca[i]=-10;
		brojbetova=new int[4];//koliko igrača je podiglo ulog bet, raise, raise,...
		brojigraca=new int[4];//broj igrača za stolom
		brojigraca1=new int[4];//broj igrača sa kartama i param u ruci
		pozicija=new int[4];//broj igrača koji su na redu posle nas
		akcija=-10;
	}

	public void parametripreflop(Igrac naredu,int brojbetova1)
	{	brojbetova[3]=brojbetova1;
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
		switch(Math.round(Ruka.pozicija(naredu,2)))
		{	case 0:
				pozicija[3]=0;
			break;
			case 1:
				pozicija[3]=1;
			break;
			case 2:case 3:
				pozicija[3]=2;
			break;
			case 4:case 5:
				pozicija[3]=3;
			break;
			default:
				pozicija[3]=4;
		}
	}

	public void parametriflopturnriver(Igrac naredu,int flop,int brojbetova1)
	{	brojbetova[flop]=brojbetova1;
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
				brojigraca[flop]=3;
		}
		switch(Math.round(Ruka.pozicija(naredu,0)))
		{	case 0:
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
		return "";
	}
}
