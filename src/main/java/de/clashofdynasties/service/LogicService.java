package de.clashofdynasties.service;

import de.clashofdynasties.logic.*;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
    private MessageRepository messageRepository;

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
            messageRepository.save();
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
            if(playerRepository.getList().stream().filter(p -> p.hasWon()).count() == 0) {
                processCities();
                processPlayer();
                processFormations();
                processCaravans();
                processDiplomacy();
            }
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

            Biome grassland = new Biome();
            grassland.setName("Grasland");
            grassland.setId(2);
            grassland.setFertilityFactor(1f);
            grassland.setProductionFactor(0.6f);
            biomeRepository.add(grassland);

            Biome coast = new Biome();
            coast.setName("Küste");
            coast.setId(3);
            coast.setFertilityFactor(0.8f);
            coast.setProductionFactor(0.6f);
            biomeRepository.add(coast);

            Biome forest = new Biome();
            forest.setName("Wald");
            forest.setId(4);
            forest.setFertilityFactor(0.7f);
            forest.setProductionFactor(1.0f);
            biomeRepository.add(forest);

            Biome mountains = new Biome();
            mountains.setName("Gebirge");
            mountains.setId(5);
            mountains.setFertilityFactor(0.5f);
            mountains.setProductionFactor(0.7f);
            biomeRepository.add(mountains);

            System.out.println("Setup: Biome eingerichtet!");
        } else {
            Biome grassland = biomeRepository.findById(2);
            if(!grassland.getName().equals("Grasland")) {
                grassland.setName("Grasland");
                grassland.setFertilityFactor(1f);
                grassland.setProductionFactor(0.6f);
            }

            Biome mountains = biomeRepository.findById(5);
            if(!mountains.getName().equals("Gebirge")) {
                mountains.setName("Gebirge");
                mountains.setFertilityFactor(0.5f);
                mountains.setProductionFactor(0.7f);
            }

            Biome coast = biomeRepository.findById(3);
            if(!coast.getName().equals("Küste")) {
                coast.setName("Küste");
                coast.setFertilityFactor(0.8f);
                coast.setProductionFactor(0.6f);
            }
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
        Biome grassland = biomeRepository.findById(2);
        Biome coast = biomeRepository.findById(3);
        Biome forest = biomeRepository.findById(4);
        Biome mountains = biomeRepository.findById(5);

        if(!mongoTemplate.collectionExists("buildingBlueprint")) {
            BuildingBlueprint house = new BuildingBlueprint();
            house.setId(1);
            house.setName("Wohnhaus");
            house.addRequiredBiome(desert);
            house.addRequiredBiome(grassland);
            house.addRequiredBiome(coast);
            house.addRequiredBiome(forest);
            house.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(house);

            BuildingBlueprint feuerwehr = new BuildingBlueprint();
            feuerwehr.setId(2);
            feuerwehr.setName("Feuerwehr");
            feuerwehr.addRequiredBiome(desert);
            feuerwehr.addRequiredBiome(grassland);
            feuerwehr.addRequiredBiome(coast);
            feuerwehr.addRequiredBiome(forest);
            feuerwehr.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(feuerwehr);

            BuildingBlueprint zentrum = new BuildingBlueprint();
            zentrum.setId(4);
            zentrum.setName("Zentrum");
            zentrum.addRequiredBiome(desert);
            zentrum.addRequiredBiome(grassland);
            zentrum.addRequiredBiome(coast);
            zentrum.addRequiredBiome(forest);
            zentrum.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(zentrum);

            BuildingBlueprint weltwunder = new BuildingBlueprint();
            weltwunder.setId(5);
            weltwunder.setName("Weltwunder");
            weltwunder.addRequiredBiome(desert);
            weltwunder.addRequiredBiome(grassland);
            weltwunder.addRequiredBiome(coast);
            weltwunder.addRequiredBiome(forest);
            weltwunder.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(weltwunder);

            BuildingBlueprint schenke = new BuildingBlueprint();
            schenke.setId(6);
            schenke.setName("Schenke");
            schenke.addRequiredBiome(desert);
            schenke.addRequiredBiome(grassland);
            schenke.addRequiredBiome(coast);
            schenke.addRequiredBiome(forest);
            schenke.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(schenke);

            BuildingBlueprint militaranlage = new BuildingBlueprint();
            militaranlage.setId(7);
            militaranlage.setName("Militäranlage");
            militaranlage.setNation(nationRepository.findById(1));
            militaranlage.addRequiredBiome(desert);
            militaranlage.addRequiredBiome(grassland);
            militaranlage.addRequiredBiome(coast);
            militaranlage.addRequiredBiome(forest);
            militaranlage.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(militaranlage);

            BuildingBlueprint verteidigungsanlage = new BuildingBlueprint();
            verteidigungsanlage.setId(8);
            verteidigungsanlage.setName("Verteidigungsanlage");
            verteidigungsanlage.setNation(nationRepository.findById(1));
            verteidigungsanlage.addRequiredBiome(desert);
            verteidigungsanlage.addRequiredBiome(grassland);
            verteidigungsanlage.addRequiredBiome(coast);
            verteidigungsanlage.addRequiredBiome(forest);
            verteidigungsanlage.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(verteidigungsanlage);

            BuildingBlueprint wachturm = new BuildingBlueprint();
            wachturm.setId(9);
            wachturm.setName("Wachturm");
            wachturm.addRequiredBiome(desert);
            wachturm.addRequiredBiome(grassland);
            wachturm.addRequiredBiome(coast);
            wachturm.addRequiredBiome(forest);
            wachturm.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(wachturm);

            BuildingBlueprint medikus = new BuildingBlueprint();
            medikus.setId(10);
            medikus.setName("Medikus");
            medikus.addRequiredBiome(desert);
            medikus.addRequiredBiome(grassland);
            medikus.addRequiredBiome(coast);
            medikus.addRequiredBiome(forest);
            medikus.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(medikus);

            BuildingBlueprint badehaus = new BuildingBlueprint();
            badehaus.setId(11);
            badehaus.setName("Badehaus");
            badehaus.addRequiredBiome(desert);
            badehaus.addRequiredBiome(grassland);
            badehaus.addRequiredBiome(coast);
            badehaus.addRequiredBiome(forest);
            badehaus.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(badehaus);

            BuildingBlueprint marktplatz = new BuildingBlueprint();
            marktplatz.setId(12);
            marktplatz.setName("Marktplatz");
            marktplatz.addRequiredBiome(desert);
            marktplatz.addRequiredBiome(grassland);
            marktplatz.addRequiredBiome(coast);
            marktplatz.addRequiredBiome(forest);
            marktplatz.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(marktplatz);

            BuildingBlueprint oper = new BuildingBlueprint();
            oper.setId(13);
            oper.setName("Oper");
            oper.setNation(nationRepository.findById(1));
            oper.addRequiredBiome(desert);
            oper.addRequiredBiome(grassland);
            oper.addRequiredBiome(coast);
            oper.addRequiredBiome(forest);
            oper.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(oper);

            BuildingBlueprint tempel = new BuildingBlueprint();
            tempel.setId(14);
            tempel.setName("Tempel");
            tempel.setNation(nationRepository.findById(2));
            tempel.addRequiredBiome(desert);
            tempel.addRequiredBiome(grassland);
            tempel.addRequiredBiome(coast);
            tempel.addRequiredBiome(forest);
            tempel.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(tempel);

            BuildingBlueprint garnison = new BuildingBlueprint();
            garnison.setId(15);
            garnison.setName("Garnison");
            garnison.setNation(nationRepository.findById(2));
            garnison.addRequiredBiome(desert);
            garnison.addRequiredBiome(grassland);
            garnison.addRequiredBiome(coast);
            garnison.addRequiredBiome(forest);
            garnison.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(garnison);

            BuildingBlueprint goldmine = new BuildingBlueprint();
            goldmine.setId(16);
            goldmine.setName("Goldmine");
            goldmine.setNation(nationRepository.findById(2));
            goldmine.addRequiredBiome(desert);
            goldmine.addRequiredBiome(grassland);
            goldmine.addRequiredBiome(coast);
            goldmine.addRequiredBiome(forest);
            goldmine.addRequiredBiome(mountains);
            goldmine.setRequiredResource(resourceRepository.findById(1));
            buildingBlueprintRepository.add(goldmine);

            BuildingBlueprint backerei = new BuildingBlueprint();
            backerei.setId(17);
            backerei.setName("Bäckerei");
            backerei.addRequiredBiome(desert);
            backerei.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(backerei);

            BuildingBlueprint fleischerei = new BuildingBlueprint();
            fleischerei.setId(18);
            fleischerei.setName("Fleischerei");
            fleischerei.addRequiredBiome(grassland);
            fleischerei.addRequiredBiome(coast);
            fleischerei.addRequiredBiome(forest);
            buildingBlueprintRepository.add(fleischerei);

            BuildingBlueprint druckerei = new BuildingBlueprint();
            druckerei.setId(19);
            druckerei.setName("Druckerei");
            druckerei.addRequiredBiome(forest);
            buildingBlueprintRepository.add(druckerei);

            BuildingBlueprint brauerei = new BuildingBlueprint();
            brauerei.setId(20);
            brauerei.setName("Brauerei");
            brauerei.setNation(nationRepository.findById(1));
            brauerei.addRequiredBiome(desert);
            brauerei.addRequiredBiome(grassland);
            brauerei.addRequiredBiome(coast);
            brauerei.addRequiredBiome(forest);
            brauerei.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(brauerei);

            BuildingBlueprint fierybrennerei = new BuildingBlueprint();
            fierybrennerei.setId(21);
            fierybrennerei.setName("Fierybrennerei");
            fierybrennerei.setNation(nationRepository.findById(2));
            fierybrennerei.addRequiredBiome(desert);
            fierybrennerei.addRequiredBiome(grassland);
            fierybrennerei.addRequiredBiome(coast);
            fierybrennerei.addRequiredBiome(forest);
            fierybrennerei.addRequiredBiome(mountains);
            buildingBlueprintRepository.add(fierybrennerei);

            BuildingBlueprint imkerei = new BuildingBlueprint();
            imkerei.setId(22);
            imkerei.setName("Imkerei");
            imkerei.setNation(nationRepository.findById(2));
            imkerei.addRequiredBiome(desert);
            imkerei.addRequiredBiome(grassland);
            imkerei.addRequiredBiome(coast);
            imkerei.addRequiredBiome(forest);
            imkerei.addRequiredBiome(mountains);
            imkerei.setRequiredResource(resourceRepository.findById(3));
            buildingBlueprintRepository.add(imkerei);

            BuildingBlueprint adyllmine = new BuildingBlueprint();
            adyllmine.setId(23);
            adyllmine.setName("Adyllmine");
            adyllmine.setNation(nationRepository.findById(1));
            adyllmine.addRequiredBiome(desert);
            adyllmine.addRequiredBiome(grassland);
            adyllmine.addRequiredBiome(coast);
            adyllmine.addRequiredBiome(forest);
            adyllmine.addRequiredBiome(mountains);
            adyllmine.setRequiredResource(resourceRepository.findById(4));
            buildingBlueprintRepository.add(adyllmine);

            BuildingBlueprint walfang = new BuildingBlueprint();
            walfang.setId(24);
            walfang.setName("Walfang");
            walfang.addRequiredBiome(desert);
            walfang.addRequiredBiome(grassland);
            walfang.addRequiredBiome(coast);
            walfang.addRequiredBiome(forest);
            walfang.addRequiredBiome(mountains);
            walfang.setRequiredResource(resourceRepository.findById(5));
            buildingBlueprintRepository.add(walfang);

            BuildingBlueprint parfumerie = new BuildingBlueprint();
            parfumerie.setId(25);
            parfumerie.setName("Parfümerie");
            parfumerie.setNation(nationRepository.findById(2));
            parfumerie.addRequiredBiome(desert);
            parfumerie.addRequiredBiome(grassland);
            parfumerie.addRequiredBiome(coast);
            parfumerie.addRequiredBiome(forest);
            parfumerie.addRequiredBiome(mountains);
            parfumerie.setProduceItem(itemRepository.findById(2));
            buildingBlueprintRepository.add(parfumerie);

            BuildingBlueprint duftmischerei = new BuildingBlueprint();
            duftmischerei.setId(26);
            duftmischerei.setName("Duftmischerei");
            duftmischerei.setNation(nationRepository.findById(1));
            duftmischerei.addRequiredBiome(desert);
            duftmischerei.addRequiredBiome(grassland);
            duftmischerei.addRequiredBiome(coast);
            duftmischerei.addRequiredBiome(forest);
            duftmischerei.addRequiredBiome(mountains);
            duftmischerei.setProduceItem(itemRepository.findById(2));
            buildingBlueprintRepository.add(duftmischerei);

            BuildingBlueprint konfiserie = new BuildingBlueprint();
            konfiserie.setId(27);
            konfiserie.setName("Konfiserie");
            konfiserie.setNation(nationRepository.findById(1));
            konfiserie.addRequiredBiome(desert);
            konfiserie.addRequiredBiome(grassland);
            konfiserie.addRequiredBiome(coast);
            konfiserie.addRequiredBiome(forest);
            konfiserie.addRequiredBiome(mountains);
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

            BuildingBlueprint weltwunder = buildingBlueprintRepository.findById(5);
            if(!weltwunder.getName().equals("Weltwunder")) {
                weltwunder.setName("Weltwunder");
            }

            BuildingBlueprint feuerwehr = buildingBlueprintRepository.findById(2);
            if(!feuerwehr.getName().equals("Feuerwehr")) {
                feuerwehr.setName("Feuerwehr");
                feuerwehr.setNation(null);
            }

            BuildingBlueprint druckerei = buildingBlueprintRepository.findById(19);
            if(druckerei.getRequiredBiomes().contains(coast))
                druckerei.removeRequiredBiome(coast);
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
