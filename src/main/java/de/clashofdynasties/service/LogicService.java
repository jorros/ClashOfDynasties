package de.clashofdynasties.service;

import de.clashofdynasties.logic.*;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class LogicService {
    @Autowired
    private CityLogic cityLogic;

    @Autowired
    private PlayerLogic playerLogic;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationLogic formationLogic;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private CaravanLogic caravanLogic;

    @Autowired
    private DiplomacyLogic diplomacyLogic;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private BiomeRepository biomeRepository;

    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private CityTypeRepository cityTypeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private long tick = 599;
    private long tickWar = 29;

    @PreDestroy
    private void saveToDatabase() {
        try {
            buildingBlueprintRepository.save();
            buildingRepository.save();
            caravanRepository.save();
            cityRepository.save();
            cityTypeRepository.save();
            eventRepository.save();
            formationRepository.save();
            playerRepository.save();
            relationRepository.save();
            roadRepository.save();
            unitBlueprintRepository.save();
            unitRepository.save();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void databaseWorker() {
        saveToDatabase();
    }

    @Scheduled(fixedDelay = 1000)
    public void Worker() {
        try {
            processCities();
            processPlayer();
            processFormations();
            processCaravans();
            processDiplomacy();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processCities() {
        List<City> cities = cityRepository.getList();
        tickWar++;

        for(City city : cities) {
            if(tickWar == 30) {
                cityLogic.processWar(city);
            }
            cityLogic.processPopulation(city);

            cityLogic.processEvents(city);

            cityLogic.processProduction(city);

            cityLogic.processCoins(city);

            cityLogic.processConstruction(city);

            cityLogic.processType(city);

            cityLogic.processHealing(city);
        }

        if(tickWar == 30) {
            tickWar = 0;
        }
    }

    private void processPlayer() {
        List<Player> players = playerRepository.getList();
        tick++;

        for(Player player : players) {
            if(tick == 600) {
                playerLogic.processStatistics(player);
                playerLogic.updateFOW(player);
            } else if(player.isSightUpdate()) {
                playerLogic.updateFOW(player);
                player.setSightUpdate(false);
            }
        }

        if(tick == 600) {
            playerLogic.processRanking();
            tick = 0;
        }
    }

    private void processFormations() {
        List<Formation> formations = formationRepository.getList();

        for(Formation formation : formations) {
            formationLogic.processMovement(formation);
            formationLogic.processMaintenance(formation);
            formationLogic.processHealing(formation);
        }
    }

    private void processCaravans() {
        List<Caravan> caravans = caravanRepository.getList();

        for(Caravan caravan : caravans) {
            if(!caravan.isPaused()) {
                caravanLogic.processMovement(caravan);
                caravanLogic.processMaintenance(caravan);
            }
        }
    }

    private void processDiplomacy() {
        List<Relation> relations = relationRepository.getList();

        for(Relation relation : relations) {
            diplomacyLogic.processTimer(relation);
        }
    }

    @PostConstruct
    public void installDatabase() {
        if(!mongoTemplate.collectionExists("biome")) {
            Biome desert = new Biome();
            desert.setName("Wüste");
            desert.setId(1);
            desert.setFertilityFactor(0.0f);
            desert.setProductionFactor(0.3f);
            biomeRepository.add(desert);

            Biome savannah = new Biome();
            savannah.setName("Savanne");
            savannah.setId(2);
            savannah.setFertilityFactor(0.5f);
            savannah.setProductionFactor(0.4f);
            biomeRepository.add(savannah);

            Biome jungle = new Biome();
            jungle.setName("Dschungel");
            jungle.setId(3);
            jungle.setFertilityFactor(0.6f);
            jungle.setProductionFactor(0.8f);
            biomeRepository.add(jungle);

            Biome forest = new Biome();
            forest.setName("Wald");
            forest.setId(4);
            forest.setFertilityFactor(0.7f);
            forest.setProductionFactor(1.0f);
            biomeRepository.add(forest);

            Biome steppe = new Biome();
            steppe.setName("Steppe");
            steppe.setId(5);
            steppe.setFertilityFactor(0.9f);
            steppe.setProductionFactor(0.3f);
            biomeRepository.add(steppe);

            System.out.println("Setup: Biome eingerichtet!");
        }

        if(!mongoTemplate.collectionExists("nation")) {
            Nation gato = new Nation();
            gato.setId(1);
            gato.setName("Gato");
            nationRepository.add(gato);

            Nation nagori = new Nation();
            nagori.setId(2);
            nagori.setName("Nagori");
            nationRepository.add(nagori);

            Nation neutral = new Nation();
            neutral.setId(3);
            neutral.setName("Neutral");
            nationRepository.add(neutral);

            System.out.println("Setup: Nation eingerichtet!");
        }

        if(!mongoTemplate.collectionExists("itemType")) {
            ItemType food = new ItemType();
            food.setId(1);
            food.setName("Nahrung");
            food.setType(1);
            itemTypeRepository.add(food);

            ItemType alcohol = new ItemType();
            alcohol.setId(2);
            alcohol.setName("Alkohol");
            alcohol.setType(2);
            itemTypeRepository.add(alcohol);

            ItemType jewelry = new ItemType();
            jewelry.setId(3);
            jewelry.setName("Schmuck");
            jewelry.setType(4);
            itemTypeRepository.add(jewelry);

            ItemType oil = new ItemType();
            oil.setId(4);
            oil.setName("Öl");
            oil.setType(3);
            itemTypeRepository.add(oil);

            ItemType perfume = new ItemType();
            perfume.setId(5);
            perfume.setName("Parfüm");
            perfume.setType(4);
            itemTypeRepository.add(perfume);

            ItemType sweets = new ItemType();
            sweets.setId(6);
            sweets.setName("Süßwaren");
            sweets.setType(3);
            itemTypeRepository.add(sweets);

            ItemType books = new ItemType();
            books.setId(7);
            books.setName("Bücher");
            books.setType(3);
            itemTypeRepository.add(books);

            System.out.println("Setup: ItemType eingerichtet!");
        }

        if(!mongoTemplate.collectionExists("item")) {
            Item meat = new Item();
            meat.setId(1);
            meat.setName("Fleisch");
            meat.setType(itemTypeRepository.findById(1));
            itemRepository.add(meat);

            Item bread = new Item();
            bread.setId(2);
            bread.setName("Brot");
            bread.setType(itemTypeRepository.findById(1));
            itemRepository.add(bread);

            Item beer = new Item();
            beer.setId(3);
            beer.setName("Bier");
            beer.setType(itemTypeRepository.findById(2));
            itemRepository.add(beer);

            Item fiery = new Item();
            fiery.setId(4);
            fiery.setName("Fiery");
            fiery.setType(itemTypeRepository.findById(2));
            itemRepository.add(fiery);

            Item perfume = new Item();
            perfume.setId(5);
            perfume.setName("Parfüm");
            perfume.setType(itemTypeRepository.findById(5));
            itemRepository.add(perfume);

            Item toiletWater = new Item();
            toiletWater.setId(6);
            toiletWater.setName("Duftwasser");
            toiletWater.setType(itemTypeRepository.findById(5));
            itemRepository.add(toiletWater);

            Item whaleOil = new Item();
            whaleOil.setId(7);
            whaleOil.setName("Walfischöl");
            whaleOil.setType(itemTypeRepository.findById(4));
            itemRepository.add(whaleOil);

            Item adyll = new Item();
            adyll.setId(8);
            adyll.setName("Adyll");
            adyll.setType(itemTypeRepository.findById(3));
            itemRepository.add(adyll);

            Item books = new Item();
            books.setId(9);
            books.setName("Bücher");
            books.setType(itemTypeRepository.findById(7));
            itemRepository.add(books);

            Item gold = new Item();
            gold.setId(10);
            gold.setName("Gold");
            gold.setType(itemTypeRepository.findById(3));
            itemRepository.add(gold);

            Item honey = new Item();
            honey.setId(11);
            honey.setName("Honig");
            honey.setType(itemTypeRepository.findById(6));
            itemRepository.add(honey);

            Item sweets = new Item();
            sweets.setId(12);
            sweets.setName("Süßwaren");
            sweets.setType(itemTypeRepository.findById(6));
            itemRepository.add(sweets);

            System.out.println("Setup: Item eingerichtet!");
        }

        if(!mongoTemplate.collectionExists("resource")) {
            Resource gold = new Resource();
            gold.setId(1);
            gold.setName("Goldvorkommen");
            resourceRepository.add(gold);

            Resource roses = new Resource();
            roses.setId(2);
            roses.setName("Rosen");
            resourceRepository.add(roses);

            Resource bees = new Resource();
            bees.setId(3);
            bees.setName("Bienen");
            resourceRepository.add(bees);

            Resource adyll = new Resource();
            adyll.setId(4);
            adyll.setName("Adyllvorkommen");
            resourceRepository.add(adyll);

            Resource whale = new Resource();
            whale.setId(5);
            whale.setName("Wal");
            resourceRepository.add(whale);

            Resource sugar = new Resource();
            sugar.setId(6);
            sugar.setName("Zucker");
            resourceRepository.add(sugar);

            System.out.println("Setup: Resource eingerichtet!");
        } else {
            Resource roses = resourceRepository.findById(2);
            if(!roses.getName().equals("Rosen")) {
                roses.setName("Rosen");
                resourceRepository.save();
            }
        }

        if(!mongoTemplate.collectionExists("cityType")) {
            CityType village = new CityType();
            village.setId(1);
            village.setName("Dorf");
            cityTypeRepository.add(village);

            CityType city = new CityType();
            city.setId(2);
            city.setName("Stadt");
            cityTypeRepository.add(city);

            CityType metropolis = new CityType();
            metropolis.setId(3);
            metropolis.setName("Großstadt");
            cityTypeRepository.add(metropolis);

            CityType tower = new CityType();
            tower.setId(4);
            tower.setName("Turm");
            cityTypeRepository.add(tower);

            System.out.println("Setup: CityType eingerichtet!");
        }

        Biome desert = biomeRepository.findById(1);
        Biome savannah = biomeRepository.findById(2);
        Biome jungle = biomeRepository.findById(3);
        Biome forest = biomeRepository.findById(4);
        Biome steppe = biomeRepository.findById(5);

        if(!mongoTemplate.collectionExists("buildingBlueprint")) {
            BuildingBlueprint house = new BuildingBlueprint();
            house.setId(1);
            house.setName("Wohnhaus");
            house.addRequiredBiome(desert);
            house.addRequiredBiome(savannah);
            house.addRequiredBiome(jungle);
            house.addRequiredBiome(forest);
            house.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(house);

            BuildingBlueprint feuerwache = new BuildingBlueprint();
            feuerwache.setId(2);
            feuerwache.setName("Feuerwache");
            feuerwache.setNation(nationRepository.findById(1));
            feuerwache.addRequiredBiome(desert);
            feuerwache.addRequiredBiome(savannah);
            feuerwache.addRequiredBiome(jungle);
            feuerwache.addRequiredBiome(forest);
            feuerwache.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(feuerwache);

            BuildingBlueprint feuerbandiger = new BuildingBlueprint();
            feuerbandiger.setId(3);
            feuerbandiger.setName("Feuerbändiger");
            feuerbandiger.setNation(nationRepository.findById(2));
            feuerbandiger.addRequiredBiome(desert);
            feuerbandiger.addRequiredBiome(savannah);
            feuerbandiger.addRequiredBiome(jungle);
            feuerbandiger.addRequiredBiome(forest);
            feuerbandiger.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(feuerbandiger);

            BuildingBlueprint zentrum = new BuildingBlueprint();
            zentrum.setId(4);
            zentrum.setName("Zentrum");
            zentrum.addRequiredBiome(desert);
            zentrum.addRequiredBiome(savannah);
            zentrum.addRequiredBiome(jungle);
            zentrum.addRequiredBiome(forest);
            zentrum.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(zentrum);

            BuildingBlueprint schule = new BuildingBlueprint();
            schule.setId(5);
            schule.setName("Schule");
            schule.addRequiredBiome(desert);
            schule.addRequiredBiome(savannah);
            schule.addRequiredBiome(jungle);
            schule.addRequiredBiome(forest);
            schule.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(schule);

            BuildingBlueprint schenke = new BuildingBlueprint();
            schenke.setId(6);
            schenke.setName("Schenke");
            schenke.addRequiredBiome(desert);
            schenke.addRequiredBiome(savannah);
            schenke.addRequiredBiome(jungle);
            schenke.addRequiredBiome(forest);
            schenke.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(schenke);

            BuildingBlueprint militaranlage = new BuildingBlueprint();
            militaranlage.setId(7);
            militaranlage.setName("Militäranlage");
            militaranlage.setNation(nationRepository.findById(1));
            militaranlage.addRequiredBiome(desert);
            militaranlage.addRequiredBiome(savannah);
            militaranlage.addRequiredBiome(jungle);
            militaranlage.addRequiredBiome(forest);
            militaranlage.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(militaranlage);

            BuildingBlueprint verteidigungsanlage = new BuildingBlueprint();
            verteidigungsanlage.setId(8);
            verteidigungsanlage.setName("Verteidigungsanlage");
            verteidigungsanlage.setNation(nationRepository.findById(1));
            verteidigungsanlage.addRequiredBiome(desert);
            verteidigungsanlage.addRequiredBiome(savannah);
            verteidigungsanlage.addRequiredBiome(jungle);
            verteidigungsanlage.addRequiredBiome(forest);
            verteidigungsanlage.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(verteidigungsanlage);

            BuildingBlueprint wachturm = new BuildingBlueprint();
            wachturm.setId(9);
            wachturm.setName("Wachturm");
            wachturm.addRequiredBiome(desert);
            wachturm.addRequiredBiome(savannah);
            wachturm.addRequiredBiome(jungle);
            wachturm.addRequiredBiome(forest);
            wachturm.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(wachturm);

            BuildingBlueprint medikus = new BuildingBlueprint();
            medikus.setId(10);
            medikus.setName("Medikus");
            medikus.addRequiredBiome(desert);
            medikus.addRequiredBiome(savannah);
            medikus.addRequiredBiome(jungle);
            medikus.addRequiredBiome(forest);
            medikus.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(medikus);

            BuildingBlueprint badehaus = new BuildingBlueprint();
            badehaus.setId(11);
            badehaus.setName("Badehaus");
            badehaus.addRequiredBiome(desert);
            badehaus.addRequiredBiome(savannah);
            badehaus.addRequiredBiome(jungle);
            badehaus.addRequiredBiome(forest);
            badehaus.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(badehaus);

            BuildingBlueprint marktplatz = new BuildingBlueprint();
            marktplatz.setId(12);
            marktplatz.setName("Marktplatz");
            marktplatz.addRequiredBiome(desert);
            marktplatz.addRequiredBiome(savannah);
            marktplatz.addRequiredBiome(jungle);
            marktplatz.addRequiredBiome(forest);
            marktplatz.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(marktplatz);

            BuildingBlueprint oper = new BuildingBlueprint();
            oper.setId(13);
            oper.setName("Oper");
            oper.setNation(nationRepository.findById(1));
            oper.addRequiredBiome(desert);
            oper.addRequiredBiome(savannah);
            oper.addRequiredBiome(jungle);
            oper.addRequiredBiome(forest);
            oper.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(oper);

            BuildingBlueprint tempel = new BuildingBlueprint();
            tempel.setId(14);
            tempel.setName("Tempel");
            tempel.setNation(nationRepository.findById(2));
            tempel.addRequiredBiome(desert);
            tempel.addRequiredBiome(savannah);
            tempel.addRequiredBiome(jungle);
            tempel.addRequiredBiome(forest);
            tempel.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(tempel);

            BuildingBlueprint garnison = new BuildingBlueprint();
            garnison.setId(15);
            garnison.setName("Garnison");
            garnison.setNation(nationRepository.findById(2));
            garnison.addRequiredBiome(desert);
            garnison.addRequiredBiome(savannah);
            garnison.addRequiredBiome(jungle);
            garnison.addRequiredBiome(forest);
            garnison.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(garnison);

            BuildingBlueprint goldmine = new BuildingBlueprint();
            goldmine.setId(16);
            goldmine.setName("Goldmine");
            goldmine.setNation(nationRepository.findById(2));
            goldmine.addRequiredBiome(desert);
            goldmine.addRequiredBiome(savannah);
            goldmine.addRequiredBiome(jungle);
            goldmine.addRequiredBiome(forest);
            goldmine.addRequiredBiome(steppe);
            goldmine.setRequiredResource(resourceRepository.findById(1));
            buildingBlueprintRepository.add(goldmine);

            BuildingBlueprint backerei = new BuildingBlueprint();
            backerei.setId(17);
            backerei.setName("Bäckerei");
            backerei.addRequiredBiome(desert);
            backerei.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(backerei);

            BuildingBlueprint fleischerei = new BuildingBlueprint();
            fleischerei.setId(18);
            fleischerei.setName("Fleischerei");
            fleischerei.addRequiredBiome(savannah);
            fleischerei.addRequiredBiome(jungle);
            fleischerei.addRequiredBiome(forest);
            buildingBlueprintRepository.add(fleischerei);

            BuildingBlueprint druckerei = new BuildingBlueprint();
            druckerei.setId(19);
            druckerei.setName("Druckerei");
            druckerei.addRequiredBiome(jungle);
            druckerei.addRequiredBiome(forest);
            buildingBlueprintRepository.add(druckerei);

            BuildingBlueprint brauerei = new BuildingBlueprint();
            brauerei.setId(20);
            brauerei.setName("Brauerei");
            brauerei.setNation(nationRepository.findById(1));
            brauerei.addRequiredBiome(desert);
            brauerei.addRequiredBiome(savannah);
            brauerei.addRequiredBiome(jungle);
            brauerei.addRequiredBiome(forest);
            brauerei.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(brauerei);

            BuildingBlueprint fierybrennerei = new BuildingBlueprint();
            fierybrennerei.setId(21);
            fierybrennerei.setName("Fierybrennerei");
            fierybrennerei.setNation(nationRepository.findById(2));
            fierybrennerei.addRequiredBiome(desert);
            fierybrennerei.addRequiredBiome(savannah);
            fierybrennerei.addRequiredBiome(jungle);
            fierybrennerei.addRequiredBiome(forest);
            fierybrennerei.addRequiredBiome(steppe);
            buildingBlueprintRepository.add(fierybrennerei);

            BuildingBlueprint imkerei = new BuildingBlueprint();
            imkerei.setId(22);
            imkerei.setName("Imkerei");
            imkerei.setNation(nationRepository.findById(2));
            imkerei.addRequiredBiome(desert);
            imkerei.addRequiredBiome(savannah);
            imkerei.addRequiredBiome(jungle);
            imkerei.addRequiredBiome(forest);
            imkerei.addRequiredBiome(steppe);
            imkerei.setRequiredResource(resourceRepository.findById(3));
            buildingBlueprintRepository.add(imkerei);

            BuildingBlueprint adyllmine = new BuildingBlueprint();
            adyllmine.setId(23);
            adyllmine.setName("Adyllmine");
            adyllmine.setNation(nationRepository.findById(1));
            adyllmine.addRequiredBiome(desert);
            adyllmine.addRequiredBiome(savannah);
            adyllmine.addRequiredBiome(jungle);
            adyllmine.addRequiredBiome(forest);
            adyllmine.addRequiredBiome(steppe);
            adyllmine.setRequiredResource(resourceRepository.findById(4));
            buildingBlueprintRepository.add(adyllmine);

            BuildingBlueprint walfang = new BuildingBlueprint();
            walfang.setId(24);
            walfang.setName("Walfang");
            walfang.addRequiredBiome(desert);
            walfang.addRequiredBiome(savannah);
            walfang.addRequiredBiome(jungle);
            walfang.addRequiredBiome(forest);
            walfang.addRequiredBiome(steppe);
            walfang.setRequiredResource(resourceRepository.findById(5));
            buildingBlueprintRepository.add(walfang);

            BuildingBlueprint parfumerie = new BuildingBlueprint();
            parfumerie.setId(25);
            parfumerie.setName("Parfümerie");
            parfumerie.setNation(nationRepository.findById(2));
            parfumerie.addRequiredBiome(desert);
            parfumerie.addRequiredBiome(savannah);
            parfumerie.addRequiredBiome(jungle);
            parfumerie.addRequiredBiome(forest);
            parfumerie.addRequiredBiome(steppe);
            parfumerie.setProduceItem(itemRepository.findById(2));
            buildingBlueprintRepository.add(parfumerie);

            BuildingBlueprint duftmischerei = new BuildingBlueprint();
            duftmischerei.setId(26);
            duftmischerei.setName("Duftmischerei");
            duftmischerei.setNation(nationRepository.findById(1));
            duftmischerei.addRequiredBiome(desert);
            duftmischerei.addRequiredBiome(savannah);
            duftmischerei.addRequiredBiome(jungle);
            duftmischerei.addRequiredBiome(forest);
            duftmischerei.addRequiredBiome(steppe);
            duftmischerei.setProduceItem(itemRepository.findById(2));
            buildingBlueprintRepository.add(duftmischerei);

            BuildingBlueprint konfiserie = new BuildingBlueprint();
            konfiserie.setId(27);
            konfiserie.setName("Konfiserie");
            konfiserie.setNation(nationRepository.findById(1));
            konfiserie.addRequiredBiome(desert);
            konfiserie.addRequiredBiome(savannah);
            konfiserie.addRequiredBiome(jungle);
            konfiserie.addRequiredBiome(forest);
            konfiserie.addRequiredBiome(steppe);
            konfiserie.setRequiredResource(resourceRepository.findById(6));
            buildingBlueprintRepository.add(konfiserie);

            System.out.println("Setup: BuildingBlueprint eingerichtet!");
        } else {
            BuildingBlueprint parfumerie = buildingBlueprintRepository.findById(25);
            BuildingBlueprint duftmischerei = buildingBlueprintRepository.findById(26);

            if(parfumerie.getRequiredResource() == null)
                parfumerie.setRequiredResource(resourceRepository.findById(2));

            if(duftmischerei.getRequiredResource() == null)
                duftmischerei.setRequiredResource(resourceRepository.findById(2));

            BuildingBlueprint marktplatz = buildingBlueprintRepository.findById(12);
            if(!marktplatz.getName().equals("Marktplatz")) {
                marktplatz.setName("Marktplatz");
                marktplatz.setNation(null);
            }

        }

        if(!mongoTemplate.collectionExists("unitBlueprint")) {
            UnitBlueprint dragoner = new UnitBlueprint();
            dragoner.setId(1);
            dragoner.setName("Dragoner");
            dragoner.setNation(nationRepository.findById(1));
            dragoner.setType(1);
            unitBlueprintRepository.add(dragoner);

            UnitBlueprint schutze = new UnitBlueprint();
            schutze.setId(2);
            schutze.setName("Berittener Schütze");
            schutze.setNation(nationRepository.findById(1));
            schutze.setType(2);
            unitBlueprintRepository.add(schutze);

            UnitBlueprint karabinier = new UnitBlueprint();
            karabinier.setId(3);
            karabinier.setName("Karabinier");
            karabinier.setNation(nationRepository.findById(1));
            karabinier.setType(3);
            unitBlueprintRepository.add(karabinier);

            UnitBlueprint feldarzt = new UnitBlueprint();
            feldarzt.setId(4);
            feldarzt.setName("Feldarzt");
            feldarzt.setNation(nationRepository.findById(1));
            feldarzt.setType(1);
            unitBlueprintRepository.add(feldarzt);

            UnitBlueprint feldartillerie = new UnitBlueprint();
            feldartillerie.setId(5);
            feldartillerie.setName("Feld-Artillerie");
            feldartillerie.setType(4);
            unitBlueprintRepository.add(feldartillerie);

            UnitBlueprint grenadier = new UnitBlueprint();
            grenadier.setId(6);
            grenadier.setName("Grenadier");
            grenadier.setNation(nationRepository.findById(2));
            grenadier.setType(1);
            unitBlueprintRepository.add(grenadier);

            UnitBlueprint berserker = new UnitBlueprint();
            berserker.setId(7);
            berserker.setName("Berserker");
            berserker.setNation(nationRepository.findById(2));
            berserker.setType(1);
            unitBlueprintRepository.add(berserker);

            UnitBlueprint gatling = new UnitBlueprint();
            gatling.setId(8);
            gatling.setName("Gatlingschütze");
            gatling.setNation(nationRepository.findById(2));
            gatling.setType(2);
            unitBlueprintRepository.add(gatling);

            UnitBlueprint drachenreiter = new UnitBlueprint();
            drachenreiter.setId(9);
            drachenreiter.setName("Drachenreiter");
            drachenreiter.setNation(nationRepository.findById(2));
            drachenreiter.setType(3);
            unitBlueprintRepository.add(drachenreiter);

            System.out.println("Setup: UnitBlueprint eingerichtet!");
        }

        if(playerRepository.getList().size() == 0) {
            Player freiesvolk = new Player();
            freiesvolk.setName("Freies Volk");
            freiesvolk.setPassword("ASDF");
            freiesvolk.setNation(nationRepository.findById(3));
            freiesvolk.setActivated(true);
            freiesvolk.setComputer(true);
            freiesvolk.setColor(0);
            freiesvolk.setSightUpdate(false);
            playerRepository.add(freiesvolk);

            System.out.println("Setup: Player eingerichtet!");

            Player newPlayer = new Player();
            newPlayer.setName("Neuer Spieler #1");
            newPlayer.setActivated(false);
            playerRepository.add(newPlayer);

            System.out.println("Neuer Spieler: " + newPlayer.getId().toHexString());
        }
    }
}
