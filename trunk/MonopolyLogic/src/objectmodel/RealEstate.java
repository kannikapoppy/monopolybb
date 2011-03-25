package objectmodel;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RealEstate
{
	private static RealEstate realEstate = null;
	private static Lock lockObject = new ReentrantLock();
	
	// Gets the single instance of the state manager
	public static RealEstate getRealEstate()
	{
		if (realEstate == null)
		{
			if (lockObject.tryLock())
			{
				if(realEstate == null)
				{
					realEstate = new RealEstate();
				}
				lockObject.unlock();
			}
		}
		
		return realEstate;
	}
	
	private Dictionary<String, Country> countries;
	private Dictionary<String, Assets> assets;
	
	private RealEstate()
	{
		countries = new Hashtable<String, Country>();
		assets = new Hashtable<String, Assets>();
	}

	public void init(GameBoard gameBoard)
	{
		for (CellBase cell : gameBoard.getCellBase())
		{
			if (cell.type.equals("City"))
			{
				City city = (City) cell;
				if(!containsKey(countries.keys(),city.getCountry().getName()))
				{
					Country newCountry = city.getCountry();
					newCountry.getCity().add(city);
					countries.put(newCountry.getName(), newCountry);
				}
				else
				{
					countries.get(city.getCountry().getName()).getCity().add(city);
				}
			}
			if (cell.type.equals("Service"))
			{
				Asset asset = (Asset) cell;
				if (!containsKey(assets.keys(), asset.getGroup().getName()))
				{
					Assets newAssetGroup = asset.getGroup();
					newAssetGroup.getAsset().add(asset);
					assets.put(newAssetGroup.getName(), newAssetGroup);
				}
				else
				{
					assets.get(asset.getGroup().getName()).getAsset().add(asset);
				}
			}
		}
	}
	
	public boolean doesOwnCountry(Player owner, String country)
	{
		boolean ownsCountry = true;
		
		for (CellBase city : countries.get(country).getCity())
		{
			if (((City) city).getOwner() != owner)
			{
				ownsCountry = false;
				break;
			}
		}
		
		return ownsCountry;
	}
	
	public boolean doesOwnAsset(Player owner, String assetGroupName)
	{
		boolean ownsAssetGroup = true;

		for (CellBase asset : assets.get(assetGroupName).getAsset())
		{
			if (((Asset) asset).getOwner() != owner)
			{
				ownsAssetGroup = false;
				break;
			}
		}
		
		return ownsAssetGroup;
	}
	
	private boolean containsKey(Enumeration<String> keys, String wantedKey)
	{
		while (keys.hasMoreElements())
		{
			if (keys.nextElement().equals(wantedKey))
			{
				return true;
			}
		} 
		return false;
	}
	
	public boolean doesOwnWholeGroup(CellBase item)
	{
		boolean ownsWhole = false;
		
		if (item.getClass() == City.class)
		{
			City city = (City) item;
			Country country = this.countries.get(city.getCountry().getName());
			ownsWhole = country.isOwnedBySameOwner();
		}
		else if (item.getClass() == Asset.class) 
		{
			Asset asset = (Asset) item;
			Assets currentAssets = this.assets.get(asset.getGroup().getName());
			ownsWhole = currentAssets.isOwnedBySameOwner();
		}
		return ownsWhole;
	}
	
	public List<CellBase> getWholeGroup(CellBase item)
	{
		List<CellBase> group = null;
		
		if(item.getClass() == City.class)
		{
			City city = (City) item;
			group = this.countries.get(city.getCountry().getName()).getCity();
		}
		else if (item.getClass() == Asset.class) 
		{
			 Asset asset = (Asset) item;
			 group = this.assets.get(asset.getGroup().getName()).getAsset();
			 
		}
		
		return group;
	}
	
	
}
