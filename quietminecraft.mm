<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1467879125437" ID="ID_1423198636" MODIFIED="1467921769582" TEXT="New Mindmap">
<node CREATED="1456401391710" ID="ID_429240407" MODIFIED="1467429279611" POSITION="right" TEXT="TheQuietMineCraft">
<node CREATED="1456401402686" ID="ID_381598210" MODIFIED="1456401409642" TEXT="Based on &quot;The Quiet Earth&quot; movie"/>
<node CREATED="1456401409942" ID="ID_1907732955" MODIFIED="1456401464570" TEXT="Worlds are visited in a certain order every time the player dies. Based on ip/username, the same world will never be visited again">
<node CREATED="1456401471943" ID="ID_694133998" MODIFIED="1456401506442" TEXT="If there is a limit to the number of worlds, an ancient world that no one visits anymore will be deleted to make room for a new one"/>
</node>
<node CREATED="1456401550230" ID="ID_1399011691" MODIFIED="1456401558850" TEXT="A portal can be built to a layer of hell, at random">
<node CREATED="1456401562982" ID="ID_1905863129" MODIFIED="1456401580850" TEXT="If a player offers gold, diamonds, or something, they can choose the layer based on the number of items"/>
</node>
<node CREATED="1456401582758" ID="ID_163144442" MODIFIED="1456401609362" TEXT="If two worlds open to the same layer of hell, players can walk between them"/>
<node CREATED="1456401669598" ID="ID_1334902554" MODIFIED="1456401685730" TEXT="When dying in hell, all souls are released on the ground next to the player"/>
<node CREATED="1456401627646" ID="ID_1291763757" MODIFIED="1456401715050" TEXT="Souls can be retrieved left from a dead player "/>
<node CREATED="1456401727662" ID="ID_1912358908" MODIFIED="1456401735322" TEXT="Portals always form close by to each other."/>
<node CREATED="1456401719238" ID="ID_637666145" MODIFIED="1456401724282" TEXT="Maybe another way to get souls?"/>
<node CREATED="1456401735654" ID="ID_912652450" MODIFIED="1456401759074" TEXT="A persistent portal always remains where it was created"/>
<node CREATED="1456401514606" ID="ID_1113304603" MODIFIED="1456401523314" TEXT="Each player gets 5 souls when starting out"/>
<node CREATED="1456401525750" ID="ID_669140945" MODIFIED="1456401652490" TEXT="When they die, they lose one soul and restart in the same world"/>
<node CREATED="1456401653094" ID="ID_811836942" MODIFIED="1456401660050" TEXT="If they run out of souls, they spawn to a new one"/>
<node CREATED="1467445003118" ID="ID_348172132" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_1085108667" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445132629" ID="ID_1843855534" MODIFIED="1467445135377" TEXT="player_id"/>
<node CREATED="1467445029110" ID="ID_1753623832" MODIFIED="1467445035189" TEXT="ip"/>
<node CREATED="1467445037534" ID="ID_1362996263" MODIFIED="1467445039395" TEXT="name"/>
<node CREATED="1467445040682" ID="ID_215217443" MODIFIED="1467445057164" TEXT="souls"/>
<node CREATED="1467445059822" ID="ID_860209672" MODIFIED="1467445064363" TEXT="world_id"/>
</node>
<node CREATED="1467445067167" ID="ID_1016253139" MODIFIED="1467445069059" TEXT="World">
<node CREATED="1467445069949" ID="ID_430708925" MODIFIED="1467445071427" TEXT="world_id"/>
<node CREATED="1467445071729" ID="ID_1567351968" MODIFIED="1467445092105" TEXT="data location or whatever"/>
</node>
<node CREATED="1467445118489" ID="ID_590870163" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1001722267" MODIFIED="1467445140325" TEXT="player_id"/>
<node CREATED="1467445144819" ID="ID_1113831247" MODIFIED="1467445154194" TEXT="world_id"/>
<node CREATED="1467445155252" ID="ID_1384876293" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1158760905" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_1229776440" MODIFIED="1467445176498" TEXT="enter"/>
<node CREATED="1467445176966" ID="ID_1530088633" MODIFIED="1467445178740" TEXT="exit"/>
<node CREATED="1467445179026" ID="ID_1210235092" MODIFIED="1467445226230" TEXT="permadeath"/>
</node>
</node>
</node>
<node CREATED="1467877033531" ID="ID_199733925" MODIFIED="1467877077979" TEXT="the same ip/username/etc can&apos;t spawn in the same world twice">
<node CREATED="1467877051820" ID="ID_1400507634" MODIFIED="1467877082591" TEXT="Even if more than one person is logging in from the same ip, it will work">
<node CREATED="1467877084395" ID="ID_1342885622" MODIFIED="1467877108471" TEXT="If two people share the same ip, it just means that each will never see each other&apos;s world "/>
</node>
</node>
</node>
<node CREATED="1467921769573" ID="ID_1041534690" MODIFIED="1467921770582" POSITION="right" TEXT="version">
<node CREATED="1467921759966" ID="ID_1668707427" MODIFIED="1467995686723" TEXT="7/7/16">
<node CREATED="1467921825795" ID="ID_288816071" MODIFIED="1467921828519" TEXT="simple"/>
<node CREATED="1467921773132" ID="ID_1732606033" MODIFIED="1467921781527" TEXT="one world, different spawn points"/>
<node CREATED="1467445003118" ID="ID_1118727661" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_26428946" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445132629" ID="ID_1609692610" MODIFIED="1467445135377" TEXT="player_id"/>
<node CREATED="1467445029110" ID="ID_1936413697" MODIFIED="1467445035189" TEXT="ip"/>
<node CREATED="1467445037534" ID="ID_526931428" MODIFIED="1467445039395" TEXT="name"/>
<node CREATED="1467921904498" ID="ID_950295170" MODIFIED="1467921916871" TEXT="last_spawn_point">
<node CREATED="1467921917971" ID="ID_785051782" MODIFIED="1467921924222" TEXT="This is the spawn point since death"/>
</node>
</node>
<node CREATED="1467445067167" ID="ID_582716704" MODIFIED="1467921843525" TEXT="Spawn Point">
<node CREATED="1467445069949" ID="ID_949751123" MODIFIED="1467921848909" TEXT="spawn_id"/>
</node>
<node CREATED="1467445118489" ID="ID_965604997" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1924141998" MODIFIED="1467445140325" TEXT="player_id"/>
<node CREATED="1467445144819" ID="ID_1738859554" MODIFIED="1467921862414" TEXT="spawn_point_id"/>
<node CREATED="1467445155252" ID="ID_1390801467" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1838503533" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_558097404" MODIFIED="1467921870493" TEXT="spawn"/>
<node CREATED="1467445176966" ID="ID_25276980" MODIFIED="1467445178740" TEXT="exit"/>
<node CREATED="1467445179026" ID="ID_1908946668" MODIFIED="1467445226230" TEXT="permadeath"/>
</node>
</node>
</node>
</node>
<node CREATED="1467995679929" ID="ID_1767029268" MODIFIED="1467995684013" TEXT="7/8/16">
<node CREATED="1467995688489" ID="ID_1610539266" MODIFIED="1473081713688" TEXT="multi world">
<node CREATED="1473081713676" ID="ID_1980286089" MODIFIED="1473081721397" TEXT="single world with multi spawn points?">
<node CREATED="1467995735168" ID="ID_221398552" MODIFIED="1473081736366" TEXT="coordinates of player can be revealed allowing players to find where they once were"/>
</node>
<node CREATED="1467445003118" ID="ID_1669710513" MODIFIED="1467921806022" TEXT="database">
<node CREATED="1467445008976" ID="ID_1726378833" MODIFIED="1467445130668" TEXT="Player">
<node CREATED="1467445037534" ID="ID_1587864216" MODIFIED="1468003618458" TEXT="uuid"/>
<node CREATED="1467445059822" ID="ID_950797792" MODIFIED="1468054942621" TEXT="world_id"/>
<node CREATED="1473782345442" ID="ID_513964811" MODIFIED="1473782350182" TEXT="last_portal_id">
<node CREATED="1473782351218" ID="ID_1538997106" MODIFIED="1473782410828" TEXT="The last portal the user logged in"/>
</node>
</node>
<node CREATED="1468054943401" ID="ID_1386911060" MODIFIED="1468054945035" TEXT="World">
<node CREATED="1468054946105" ID="ID_1671473401" MODIFIED="1468054952069" TEXT="id"/>
<node CREATED="1468054952433" ID="ID_1289061435" MODIFIED="1468054954029" TEXT="name"/>
</node>
<node CREATED="1473782106042" ID="ID_561265688" MODIFIED="1473782108071" TEXT="VisitedWorld">
<node CREATED="1473782109498" ID="ID_1318187908" MODIFIED="1473782110270" TEXT="id"/>
<node CREATED="1473782221074" ID="ID_1926893251" MODIFIED="1473782224862" TEXT="nickname">
<node CREATED="1473782226450" ID="ID_1929490821" MODIFIED="1473782252238" TEXT="Used to give the world a random name"/>
</node>
<node CREATED="1473782110794" ID="ID_1040217542" MODIFIED="1473782127574" TEXT="nether_portal_loc[x,y,z]"/>
<node CREATED="1473782128058" ID="ID_89568102" MODIFIED="1473782136270" TEXT="span_loc[x,y,z]"/>
</node>
<node CREATED="1473782137626" ID="ID_1718827732" MODIFIED="1473782140334" TEXT="PortalLink">
<node CREATED="1473782141490" ID="ID_105936424" MODIFIED="1473782143478" TEXT="id"/>
<node CREATED="1473782145002" ID="ID_1545534943" MODIFIED="1473782170414" TEXT="loc1_vw"/>
<node CREATED="1473782150882" ID="ID_1088672852" MODIFIED="1473782156118" TEXT="loc1[x,y,z]">
<node CREATED="1473782189322" ID="ID_550703063" MODIFIED="1473782201214" TEXT="block (int) location of bottom center portal element"/>
</node>
<node CREATED="1473782145002" ID="ID_194372849" MODIFIED="1473782181702" TEXT="loc2_vw"/>
<node CREATED="1473782150882" ID="ID_539080733" MODIFIED="1473782188118" TEXT="loc2[x,y,z]">
<node CREATED="1473782189322" ID="ID_812788050" MODIFIED="1473782201214" TEXT="block (int) location of bottom center portal element"/>
</node>
</node>
<node CREATED="1467445118489" ID="ID_1944689299" MODIFIED="1467445126463" TEXT="PlayerLog">
<node CREATED="1467445138537" ID="ID_1787179342" MODIFIED="1468003630410" TEXT="player_uuid"/>
<node CREATED="1467445144819" ID="ID_1836162242" MODIFIED="1468054939235" TEXT="world_id"/>
<node CREATED="1467445155252" ID="ID_50553467" MODIFIED="1467445263089" TEXT="datetime"/>
<node CREATED="1467445165533" ID="ID_1060242751" MODIFIED="1467445166971" TEXT="action">
<node CREATED="1467445174836" ID="ID_693366832" MODIFIED="1468005051817" TEXT="login"/>
<node CREATED="1467445176966" ID="ID_646167573" MODIFIED="1468005053993" TEXT="logout"/>
<node CREATED="1468005070125" ID="ID_403353019" MODIFIED="1468005076649" TEXT="new_spawn">
<node CREATED="1468005078365" ID="ID_251825192" MODIFIED="1468005083393" TEXT="first time to spawn in a world"/>
</node>
<node CREATED="1467445179026" ID="ID_577635901" MODIFIED="1467445226230" TEXT="permadeath">
<node CREATED="1468005088581" ID="ID_775344642" MODIFIED="1468005101385" TEXT="player died and is removed from this world forever"/>
</node>
</node>
</node>
</node>
<node CREATED="1468004720758" ID="ID_1245911041" MODIFIED="1468004724361" TEXT="in memory">
<node CREATED="1468004699429" ID="ID_1089857093" MODIFIED="1468005000195" TEXT="active worlds">
<node CREATED="1468004709765" ID="ID_104090092" MODIFIED="1468005011649" TEXT="player_count"/>
</node>
</node>
</node>
<node CREATED="1467996547870" ID="ID_1741852940" MODIFIED="1467996552931" TEXT="startup">
<node CREATED="1467996554087" ID="ID_1545746531" MODIFIED="1467996557883" TEXT="create/open database"/>
<node CREATED="1473081969417" ID="ID_845212550" MODIFIED="1473081976021" TEXT="initialize worlds"/>
</node>
<node CREATED="1467996559071" ID="ID_664342422" MODIFIED="1467996562123" TEXT="on player login">
<node CREATED="1467996564439" ID="ID_1175726893" MODIFIED="1467996567715" TEXT="lookup of player"/>
<node CREATED="1467996573359" ID="ID_1505544654" MODIFIED="1467996578074" TEXT="if player found">
<node CREATED="1467996579499" ID="ID_123843905" MODIFIED="1473081906093" TEXT="player is assumed to be in the right place, so we don&apos;t move them"/>
<node CREATED="1473081908345" ID="ID_1419504634" MODIFIED="1473081916621" TEXT="record to db action"/>
</node>
<node CREATED="1467996601421" ID="ID_601103134" MODIFIED="1467996613070" TEXT="else">
<node CREATED="1467996613983" ID="ID_12034274" MODIFIED="1467996617115" TEXT="create new player"/>
<node CREATED="1467996617431" ID="ID_98625611" MODIFIED="1468004307986" TEXT="choose world">
<node CREATED="1468004311646" ID="ID_1107200345" MODIFIED="1468004315849" TEXT="suitable world">
<node CREATED="1468004324798" ID="ID_1690292589" MODIFIED="1468004348585" TEXT="contains the most active people of all worlds"/>
<node CREATED="1468004349317" ID="ID_1545438265" MODIFIED="1468004363089" TEXT="of equal active people, contains the most people that ever visited"/>
</node>
</node>
</node>
<node CREATED="1467996645991" ID="ID_510693726" MODIFIED="1467996651475" TEXT="update playerlog"/>
</node>
<node CREATED="1468004532414" ID="ID_731819014" MODIFIED="1468004539393" TEXT="options">
<node CREATED="1468004540550" ID="ID_1509246602" MODIFIED="1468004562897" TEXT="Should we make each world a copy of the world the person died in?">
<node CREATED="1468004570446" ID="ID_1723863330" MODIFIED="1468004606721" TEXT="When the player spawns in the new world, its in a slightly different place, incase the spawn point is some sort of trap room"/>
<node CREATED="1468004624709" ID="ID_1512606921" MODIFIED="1468004638353" TEXT="This way all the work the player did wouldn&apos;t be lost, but the people would be gone."/>
<node CREATED="1468004655573" ID="ID_20847600" MODIFIED="1468004670355" TEXT="Would only occur if a new world was built for the player personally"/>
</node>
</node>
<node CREATED="1473080306960" ID="ID_1727806319" MODIFIED="1473099187029" TEXT="spawning">
<node CREATED="1473080310848" ID="ID_722706054" MODIFIED="1473764860073" TEXT="when a player dies, with no souls  they go to the next world they haven&apos;t visited yet">
<node CREATED="1473080329800" ID="ID_11412421" MODIFIED="1473080334965" TEXT="If the player has visited all the worlds then">
<node CREATED="1473080335880" ID="ID_1533665923" MODIFIED="1473080348765" TEXT="If there are any worlds that haven&apos;t been visited in a month, the player can join those"/>
<node CREATED="1473080349120" ID="ID_133161436" MODIFIED="1473080365052" TEXT="Otherwise the player goes to nether"/>
</node>
</node>
</node>
<node CREATED="1473099187689" ID="ID_1690179985" MODIFIED="1473099211850" TEXT="server">
<node CREATED="1473099211830" ID="ID_744280517" MODIFIED="1473099213036" TEXT="vultr">
<node CREATED="1473099195536" ID="ID_1971885085" MODIFIED="1473099200787" TEXT="$60 per month">
<node CREATED="1473099202033" ID="ID_386042811" MODIFIED="1473099203621" TEXT="8 gig"/>
<node CREATED="1473099204552" ID="ID_1739776195" MODIFIED="1473099205660" TEXT="2 cpu"/>
<node CREATED="1473099206328" ID="ID_15722012" MODIFIED="1473099209084" TEXT="120 GB"/>
<node CREATED="1473099339832" ID="ID_253230457" MODIFIED="1473099343213" TEXT="10 TB bandwidth"/>
</node>
<node CREATED="1473099286913" ID="ID_1878431273" MODIFIED="1473099290772" TEXT="about 100 players"/>
</node>
</node>
<node CREATED="1473099421192" ID="ID_842966636" MODIFIED="1473764973238" TEXT="thoughts">
<node CREATED="1473099425896" ID="ID_1629893461" MODIFIED="1473099480125" TEXT="If we have a maximum of 50 worlds, it seems more like we are just a perma death server with extra lives">
<node CREATED="1473099481585" ID="ID_1603276191" MODIFIED="1473099510045" TEXT="All we need to do is prevent the high death users from joining servers populated by more friendly people."/>
<node CREATED="1473099517672" ID="ID_1960633103" MODIFIED="1473099540868" TEXT="Which means we can reuse as many worlds as we want. "/>
</node>
<node CREATED="1473147408517" ID="ID_1217376109" MODIFIED="1473147436571" TEXT="If the player dies and goes to the nether, then what? Is he nether forever?">
<node CREATED="1473147526518" ID="ID_1953966969" MODIFIED="1473147544482" TEXT="As time goes by and worlds are abandoned, they become revistable"/>
<node CREATED="1473147546509" ID="ID_545265495" MODIFIED="1473147561155" TEXT="People are banned from worlds for a limited period of time???">
<node CREATED="1473147567109" ID="ID_817469872" MODIFIED="1473147578497" TEXT="Banned until they are abandoned for a particular period of time"/>
</node>
<node CREATED="1473147590037" ID="ID_473333231" MODIFIED="1473147605913" TEXT="So people get a set of worlds they are allowed to visit when they die"/>
<node CREATED="1473147608173" ID="ID_574352667" MODIFIED="1473147621378" TEXT="Everytime they die they lose the ability to revisit the world through death">
<node CREATED="1473147622662" ID="ID_138449464" MODIFIED="1473147635986" TEXT="If the world is abandoned long enough, it resets for all players?">
<node CREATED="1473147682101" ID="ID_868913248" MODIFIED="1473147701730" TEXT="Yes, and probably gets a new spawn point, nearby, but not too close to the original spawn point"/>
<node CREATED="1473147723501" ID="ID_1865020102" MODIFIED="1473147739443" TEXT="So a world gets a visit_id">
<node COLOR="#999999" CREATED="1473147740797" ID="ID_996779680" MODIFIED="1473147814165">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      Each player starts with a visit_id of 0
    </p>
  </body>
</html></richcontent>
<font NAME="SansSerif" SIZE="10"/>
</node>
<node CREATED="1473147863533" ID="ID_54278056" MODIFIED="1473147892603" TEXT="Player logs record which visit_ids the player has seen"/>
<node CREATED="1473147893133" ID="ID_1851855622" MODIFIED="1473147913227" TEXT="If a player runs out of visit_ids, they go to nether"/>
</node>
</node>
</node>
</node>
<node CREATED="1473148032502" ID="ID_663027019" MODIFIED="1473148035937" TEXT="monetizing">
<node CREATED="1473148037638" ID="ID_1447795321" MODIFIED="1473148042634" TEXT="beta period then subscription fee">
<node CREATED="1473148049678" ID="ID_1028255873" MODIFIED="1473148058569" TEXT="don&apos;t mention subscription period until beta ends"/>
<node CREATED="1473148071613" ID="ID_1653948769" MODIFIED="1473148107355" TEXT="subscription cheap. $4.99 a month or something?"/>
<node CREATED="1473148108350" ID="ID_1594113529" MODIFIED="1473148115635" TEXT="newcomers get trial period"/>
</node>
</node>
<node CREATED="1473332784744" FOLDED="true" ID="ID_1272767155" MODIFIED="1473779646629" TEXT="portals">
<node CREATED="1473332791007" ID="ID_927903694" MODIFIED="1473332799738" TEXT="there should only be one active portal per world">
<node CREATED="1473332800726" ID="ID_956590528" MODIFIED="1473332810354" TEXT="if user creates another, the previous portal is deactivated"/>
</node>
<node CREATED="1473332822574" ID="ID_1436607596" MODIFIED="1473332839346" TEXT="portals from a world always appear in the same place in the nether?">
<node CREATED="1473332840238" ID="ID_437514496" MODIFIED="1473332853682" TEXT="what if the nether location is flooded with lava, etc?"/>
<node CREATED="1473332885911" ID="ID_762527727" MODIFIED="1473332914202" TEXT="someone may booby trap nether portal for a world, or for all worlds they can">
<node CREATED="1473332917086" ID="ID_1906824224" MODIFIED="1473332921234" TEXT="is this acceptable?">
<node CREATED="1473332952774" ID="ID_1709771872" MODIFIED="1473332978716" TEXT="I think yes but when worlds are revisted, their &quot;nether spawn&quot; point is randomized"/>
<node CREATED="1473333006966" ID="ID_32447400" MODIFIED="1473333018706" TEXT="This provides a lot of interesting permanant world changes">
<node CREATED="1473333019526" ID="ID_708095488" MODIFIED="1473333034074" TEXT="Someone could become &quot;king of hell&quot; by controlling all the world ports"/>
</node>
</node>
</node>
</node>
<node CREATED="1473333072158" ID="ID_914297837" MODIFIED="1473333078970" TEXT="the balance of power is somewhat scary.">
<node CREATED="1473333079934" ID="ID_1171876590" MODIFIED="1473333119348" TEXT="A player who acts nice, could enter a world, create a nether portal in a hidden location, allowing all his evil friends to come in away from the spawn point">
<node CREATED="1473333150838" ID="ID_1141185602" MODIFIED="1473333163651" TEXT="This is too much potential for destruction"/>
</node>
</node>
<node CREATED="1473333201447" ID="ID_867114380" MODIFIED="1473333219354" TEXT="Maybe first user gets a special item that only he can use to create portals">
<node CREATED="1473333221150" ID="ID_830564560" MODIFIED="1473333234986" TEXT="A special flint and steel, that only works for that world and lights a portal">
<node CREATED="1473333670151" ID="ID_1603563414" MODIFIED="1473333678979" TEXT="We will need special fire?"/>
<node CREATED="1473333680887" ID="ID_807915352" MODIFIED="1473333685428" TEXT="This may be difficult to implement"/>
</node>
<node CREATED="1473333286271" ID="ID_1554586392" MODIFIED="1473333322482" TEXT="This way there could still be a &quot;king of hell&quot;, but it&apos;d be a lot harder to get worlds to open up portals until they are ready"/>
<node CREATED="1473333519167" ID="ID_985951557" MODIFIED="1473333532683" TEXT="But then, what if the user logs off and never logs in again?">
<node CREATED="1473333540943" ID="ID_1295995595" MODIFIED="1473333547882" TEXT="Or buries it"/>
<node CREATED="1473333570815" ID="ID_1118879777" MODIFIED="1473333668059" TEXT="We can make the special item drop when they log off, I think"/>
</node>
</node>
<node CREATED="1473333780623" ID="ID_1373267425" MODIFIED="1473333792891" TEXT="Maybe everyone gets a special portal block">
<node CREATED="1473333794015" ID="ID_1269758791" MODIFIED="1473333812451" TEXT="It takes a certain number of special portal blocks to open a portal"/>
</node>
<node CREATED="1473333829559" ID="ID_1534974046" MODIFIED="1473333866275" TEXT="Maybe only the oldest alive member on the server can enter a portal">
<node CREATED="1473333871375" ID="ID_1436227938" MODIFIED="1473333884067" TEXT="This &quot;activates&quot; the portal, and then the rest of the members can"/>
<node CREATED="1473333884759" ID="ID_578651351" MODIFIED="1473333935282" TEXT="However, that would still be bad, if most people are logged off, a rogue player could then still create and enter a portal of their own making"/>
</node>
<node CREATED="1473333988919" ID="ID_811637881" MODIFIED="1473334006835" TEXT="I think a portal key, or a special flint of steel would be best">
<node CREATED="1473334008927" ID="ID_110367794" MODIFIED="1473334036338" TEXT="If we can&apos;t make a special flint of steel work, we just make the first player that enters a portal activate it (and they must have the portal key)"/>
<node CREATED="1473334041319" ID="ID_115578355" MODIFIED="1473334050555" TEXT="It someone buries it, thats the way it goes."/>
</node>
<node CREATED="1473335252704" ID="ID_99279135" MODIFIED="1473335268451" TEXT="a portal key in hell can be used to create a portal to a specific world">
<node CREATED="1473335270023" ID="ID_1895819666" MODIFIED="1473335290715" TEXT="I think it should be a one way portal, and teleports them randomly within the world">
<node CREATED="1473335292127" ID="ID_668939824" MODIFIED="1473335313683" TEXT="This way, &quot;the devil&quot;, or whoever wants to be a king of hell can visit worlds if they get a portal key"/>
<node CREATED="1473335315519" ID="ID_773715383" MODIFIED="1473335384460" TEXT="Since the portal is still open on hell&apos;s side, it will allow others to come, too. They just can&apos;t get back (unless of course they die, which means they automatically go back), or if the owner of the portal key makes another portal on the world side"/>
</node>
</node>
<node CREATED="1473335418744" ID="ID_129545511" MODIFIED="1473335425219" TEXT="Maybe portals should all be one way.">
<node CREATED="1473335426336" ID="ID_1184933579" MODIFIED="1473335447548" TEXT="If the portal key holder wants to go back, they need to carry the items necessary to create a portal back to their world"/>
<node CREATED="1473335450047" ID="ID_508157317" MODIFIED="1473335477683" TEXT="This would complicate getting a friend back to a world they died in">
<node CREATED="1473335478976" ID="ID_1958660370" MODIFIED="1473335506733" TEXT="The PKH would have to open a portal, go find them in hell, and they both come back together"/>
<node CREATED="1473335516104" ID="ID_1226921339" MODIFIED="1473335541260" TEXT="If they died in hell, they&apos;d both be dead and there would be no way to get back, unless someone could recover the key"/>
<node CREATED="1473335561104" ID="ID_740422333" MODIFIED="1473335568348" TEXT="Maybe this would be better than having a static spawn point">
<node CREATED="1473335569656" ID="ID_1910508114" MODIFIED="1473335579980" TEXT="If the devil gets your key, your screwed, in other words">
<node CREATED="1473335609640" ID="ID_249537275" MODIFIED="1473335620188" TEXT="Your whole world would have a random spawn point to it from the nether."/>
</node>
</node>
</node>
<node CREATED="1473335687280" ID="ID_1612136858" MODIFIED="1473335721596" TEXT="What if we remove the &quot;only one portal active&quot; rule and have random but consistent spawn points">
<node CREATED="1473335723136" ID="ID_12520458" MODIFIED="1473335748076" TEXT="Then someone could even set up a portals between overworlds">
<node CREATED="1473335749384" ID="ID_504118464" MODIFIED="1473335761252" TEXT="Create portal to nether with k1"/>
<node CREATED="1473335761656" ID="ID_1306271311" MODIFIED="1473335767100" TEXT="Enter nether"/>
<node CREATED="1473335768552" ID="ID_889749172" MODIFIED="1473335781636" TEXT="Go through portal to world 2"/>
<node CREATED="1473335782088" ID="ID_182330312" MODIFIED="1473335792436" TEXT="Create portal using world one portal key"/>
</node>
</node>
</node>
<node CREATED="1473335834696" ID="ID_1153869783" MODIFIED="1473335862483" TEXT="Portals could still be two way. It just prevents mining obsidian, which I don&apos;t care too much about (since it&apos;s possible in vanilla anyway)">
<node CREATED="1473335863816" ID="ID_553608000" MODIFIED="1473335873476" TEXT="With two way portals and a portal key...">
<node CREATED="1473335918848" ID="ID_401645410" MODIFIED="1473335931812" TEXT="The portal holder wouldn&apos;t have to go into hell (and not first)">
<node CREATED="1473335935344" ID="ID_673447100" MODIFIED="1473335949900" TEXT="This may be better because the portal to hell may be unfair for some reason and cause instant death"/>
</node>
<node CREATED="1473335962592" ID="ID_116922802" MODIFIED="1473335970235" TEXT="Or they could go in, but have to jump right out"/>
</node>
</node>
<node CREATED="1473349983851" ID="ID_98650745" MODIFIED="1473350442952" TEXT="what if a portal key just made a portal appear on the other side as well?">
<node CREATED="1473350001995" ID="ID_22803797" MODIFIED="1473350047879" TEXT="That way we still can have both sides open."/>
<node CREATED="1473350051571" ID="ID_1332274588" MODIFIED="1473350068566" TEXT="We can check if the player has the key in inventory. If not, its a one way portal, bye player!"/>
<node CREATED="1473350069179" ID="ID_1329483500" MODIFIED="1473350079038" TEXT="If they do, then the portal is created on the other side"/>
<node CREATED="1473350084395" ID="ID_1850191999" MODIFIED="1473350102367" TEXT="Other players, entities, can then enter the portal after it has been created"/>
<node CREATED="1473350298467" ID="ID_3828311" MODIFIED="1473350316103" TEXT="It would be confusing though, and if a player made a mistake, they&apos;d be trapped out of their world forever. Kind of sad"/>
</node>
<node CREATED="1473350444043" ID="ID_1977839165" MODIFIED="1473350463543" TEXT="What if we don&apos;t have portal keys. Instead, only one portal can work?">
<node CREATED="1473350464411" ID="ID_1311843530" MODIFIED="1473350495343" TEXT="Then hell entities always have access to world as long as a portal exists"/>
</node>
<node CREATED="1473350514283" ID="ID_546038631" MODIFIED="1473350556599" TEXT="If we have a special flint and steel, and we just find the player around the portal. If they have a special flint and steel, we assume the portal is proper and create it, otherwise don&apos;t/ explode/ whatever">
<node CREATED="1473350576291" ID="ID_756455142" MODIFIED="1473350591319" TEXT="It&apos;s clear, then. The special flint and steel creates portals. Normal ones don&apos;t"/>
</node>
<node CREATED="1473759370420" ID="ID_1135922241" MODIFIED="1473759385367" TEXT="portal destruction">
<node CREATED="1473759387035" ID="ID_1390198332" MODIFIED="1473759396599" TEXT="If one side of a portal is destroyed, should we destroy the other side?">
<node CREATED="1473760137835" ID="ID_1094931192" MODIFIED="1473760161567" TEXT="I like this idea, because it adds a lot of surprise to the game if suddenly a portal is destroyed"/>
</node>
</node>
<node CREATED="1473759397571" ID="ID_12698110" MODIFIED="1473759403815" TEXT="multiple portals to nether">
<node CREATED="1473759414835" ID="ID_735747390" MODIFIED="1473760029215" TEXT="If we create multiple portals from a world to the nether, what should happen?">
<node CREATED="1473760029206" ID="ID_1317689360" MODIFIED="1473760037495" TEXT="Keep multiple portals open?">
<node CREATED="1473759960147" ID="ID_561947213" MODIFIED="1473760016479" TEXT="I like multiple portals, because it allows a person to create a portal secretly, go to the nether, do some business there, come back, and destroy the other portal"/>
<node CREATED="1473760039819" ID="ID_1809405283" MODIFIED="1473760044175" TEXT="Teleport back randomly?">
<node CREATED="1473760056435" ID="ID_150878578" MODIFIED="1473760081367" TEXT="Maybe give each player a &quot;last&quot; portallink field, so they come back the way they came">
<node CREATED="1473760083515" ID="ID_183570851" MODIFIED="1473760094255" TEXT="Maybe add some randomness to it, so you might go to the other portal"/>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1473759406883" ID="ID_1755442873" MODIFIED="1473759413047" TEXT="portals from nether">
<node CREATED="1473759467427" ID="ID_153451036" MODIFIED="1473759477791" TEXT="Should portals from nether be wild?">
<node CREATED="1473759848187" ID="ID_461202264" MODIFIED="1473759857015" TEXT="Yes, because it means that losing your portal key is bad"/>
</node>
</node>
</node>
<node CREATED="1473762235117" ID="ID_1936397530" MODIFIED="1473762239552" TEXT="world reborning">
<node CREATED="1473762241012" ID="ID_766962356" MODIFIED="1473762262857" TEXT="If a player was the only one to visit a world, can we allow the user to revisit it?"/>
<node CREATED="1473762272676" ID="ID_1600885092" MODIFIED="1473762494719" TEXT="Under what circumstances does a world become revisitable?"/>
</node>
</node>
<node CREATED="1473779670385" ID="ID_1806096186" MODIFIED="1473780476520" TEXT="thoughts2">
<node CREATED="1473779673521" ID="ID_679005881" MODIFIED="1473779679093" TEXT="world reuse">
<node CREATED="1473779680105" ID="ID_1178543141" MODIFIED="1473779729933" TEXT="not eligible if">
<node CREATED="1473779684209" ID="ID_714270803" MODIFIED="1473779691333" TEXT="players existing in world">
<node CREATED="1473779692401" ID="ID_98781438" MODIFIED="1473779697757" TEXT="logged in"/>
<node CREATED="1473779698361" ID="ID_745508255" MODIFIED="1473779706669" TEXT="logged out but within X days">
<node CREATED="1473780600449" ID="ID_1125172592" MODIFIED="1473780623373" TEXT="Probably 7 days"/>
</node>
</node>
<node CREATED="1473779708281" ID="ID_1228561549" MODIFIED="1473779726421" TEXT="portals exist"/>
<node CREATED="1473779894833" ID="ID_570379573" MODIFIED="1473781964342" TEXT="player left from a portal within X days">
<node CREATED="1473779904921" ID="ID_930017209" MODIFIED="1473779912125" TEXT="probably 1 day or less"/>
<node CREATED="1473780545441" ID="ID_808436687" MODIFIED="1473780582389" TEXT="This prevents the case where everyone in the world take a trip to the nether together, and destroy the portal, since they know they can come back since they have a portal key"/>
</node>
</node>
<node CREATED="1473779873601" ID="ID_1205514430" MODIFIED="1473779880661" TEXT="worlds will be reused oldest first"/>
<node CREATED="1473780627921" ID="ID_1527181915" MODIFIED="1473780639453" TEXT="players that log in from an old visiting world">
<node CREATED="1473780645834" ID="ID_1490906036" MODIFIED="1473780647869" TEXT="What do we do?">
<node CREATED="1473780649665" ID="ID_315100314" MODIFIED="1473780660685" TEXT="Give them a new portal key?">
<node CREATED="1473780662737" ID="ID_1270185479" MODIFIED="1473780666853" TEXT="What if they didn&apos;t have one?"/>
<node CREATED="1473780668025" ID="ID_1397679606" MODIFIED="1473780674165" TEXT="Check their inventory and replace it?"/>
<node CREATED="1473780675273" ID="ID_1292870095" MODIFIED="1473780698621" TEXT="What if they stored it in a box somewhere?"/>
</node>
</node>
</node>
</node>
<node CREATED="1473779739601" ID="ID_1129327386" MODIFIED="1473779867733" TEXT="from other world (including from nether) portals">
<node CREATED="1473779760681" ID="ID_453209071" MODIFIED="1473779786477" TEXT="do not work if world has been reclaimed"/>
</node>
<node CREATED="1473780294697" ID="ID_1548407758" MODIFIED="1473780298717" TEXT="remove QCLocation">
<node CREATED="1473780299873" ID="ID_1150194141" MODIFIED="1473780304725" TEXT="It&apos;s too messy">
<node CREATED="1473780305873" ID="ID_1071834436" MODIFIED="1473780310037" TEXT="locations pointing to the same place"/>
<node CREATED="1473780317137" ID="ID_1459154536" MODIFIED="1473780330334" TEXT="having to clean up locations everytime we delete a visited world or portal link"/>
<node CREATED="1473780330609" ID="ID_1775631448" MODIFIED="1473780338997" TEXT="extra joins to find portal links referencing the same place"/>
</node>
</node>
<node CREATED="1473780476513" ID="ID_198195587" MODIFIED="1473780493485" TEXT="Portals need to be created at chosen block, not &quot;nearby&quot;">
<node CREATED="1473780341057" ID="ID_841562490" MODIFIED="1473780358702" TEXT="portal representatitive location now min corner">
<node CREATED="1473780360537" ID="ID_1264874952" MODIFIED="1473780373677" TEXT="Otherwise different portals of different sizes will have different locations"/>
</node>
<node CREATED="1473780409457" ID="ID_498453880" MODIFIED="1473780513101" TEXT="What about all portals have odd width?">
<node CREATED="1473780500074" ID="ID_1079092045" MODIFIED="1473780518189" TEXT="Then we can still create the portal based on the chosen block"/>
<node CREATED="1473780520457" ID="ID_860420728" MODIFIED="1473780529397" TEXT="And teleportation won&apos;t be so painful"/>
</node>
</node>
<node CREATED="1473780724769" ID="ID_1071776257" MODIFIED="1473781131094" TEXT="people spawning in nether">
<node CREATED="1473780730554" ID="ID_1277777876" MODIFIED="1473780740605" TEXT="hard to survive in the nether">
<node CREATED="1473780742689" ID="ID_1491446246" MODIFIED="1473781145357" TEXT="give them a welcoming back of iron tools, weapons, etc">
<node CREATED="1473781149449" ID="ID_1004443840" MODIFIED="1473781833236" TEXT="not too good, but not so bad they have to give up"/>
<node CREATED="1473781736850" ID="ID_1286864581" MODIFIED="1473781748014" TEXT="gives them a reason to keep killing themselves"/>
<node CREATED="1473781748322" ID="ID_1791198353" MODIFIED="1473781768174" TEXT="If they spawn somewhere else... but still, yes a problem"/>
</node>
<node CREATED="1473781834474" ID="ID_1878465487" MODIFIED="1473781894694" TEXT="give them a magic device that gives them information of where portals, other people, etc. are when &quot;used&quot;?"/>
</node>
<node CREATED="1473781157473" ID="ID_292790336" MODIFIED="1473781165094" TEXT="can&apos;t find living people easily">
<node CREATED="1473781167169" ID="ID_1216739459" MODIFIED="1473781214717" TEXT="Automatic messaging system indicating direction of new portals">
<node CREATED="1473781215666" ID="ID_1031207668" MODIFIED="1473781220085" TEXT="Have to wait for a portal"/>
<node CREATED="1473781220777" ID="ID_435292919" MODIFIED="1473781240599" TEXT="compass doesn&apos;t work"/>
</node>
<node CREATED="1473781670010" ID="ID_1794852017" MODIFIED="1473781679494" TEXT="spawn near portals?">
<node CREATED="1473781681834" ID="ID_804915481" MODIFIED="1473781682814" TEXT="evil"/>
<node CREATED="1473782032674" ID="ID_828092991" MODIFIED="1473782036718" TEXT="too easy I think"/>
<node CREATED="1473782043674" ID="ID_1949293140" MODIFIED="1473782049974" TEXT="maybe a certain distance, though, would be ok"/>
</node>
</node>
<node CREATED="1473782071266" ID="ID_1579837478" MODIFIED="1473782089350" TEXT="people spawning in nether shouldn&apos;t happen too often, so we&apos;ll make this future"/>
</node>
<node CREATED="1473780870545" ID="ID_1939626376" MODIFIED="1473780885981" TEXT="nether portals should have a ceiling to prevent falling lava"/>
</node>
<node CREATED="1473329224300" ID="ID_999690107" MODIFIED="1473329225537" TEXT="portals">
<node CREATED="1473344962057" ID="ID_224671769" MODIFIED="1473759613455" TEXT="each user gets a special portal key. when a portal is made, this key must be nearby">
<node CREATED="1473345017233" ID="ID_596003341" MODIFIED="1473345050380" TEXT="Portals made in the nether with this block will only go to the world of the portal block"/>
<node CREATED="1473759759963" ID="ID_365677963" MODIFIED="1473759788711" TEXT="This prevents a random user from opening up a portal to hell and letting other random users inside the world">
<node CREATED="1473759789924" ID="ID_1835980555" MODIFIED="1473759799591" TEXT="The user would have to be coaxed to give up their portal key"/>
</node>
<node CREATED="1473764869141" ID="ID_1817584664" MODIFIED="1473764879817" TEXT="Portal keys are associated with visited world, not actual world">
<node CREATED="1473764881525" ID="ID_1436586991" MODIFIED="1473764897281" TEXT="When world is reused, portal key from old world no longer works"/>
</node>
</node>
<node CREATED="1473759623867" ID="ID_1924238193" MODIFIED="1473759665815" TEXT="portals from each world go only to one &quot;nether spawn point&quot; for that world">
<node CREATED="1473759666907" ID="ID_471626626" MODIFIED="1473777788804" TEXT="This way, someone in the nether can control the world nether portals and become &quot;king of the nether&quot;">
<node CREATED="1473759700867" ID="ID_45020791" MODIFIED="1473759743199" TEXT="By strangling the portal to the nether, the world will slowly die, since newbies will run out, and souls that are lost cannot be regained"/>
</node>
</node>
<node CREATED="1473759862315" ID="ID_581196899" MODIFIED="1473759877175" TEXT="portals from nether can be created, too, as long as a player has a portal key">
<node CREATED="1473759878451" ID="ID_242091547" MODIFIED="1473759904903" TEXT="This means that losing a portal key can be dangerous"/>
</node>
</node>
<node CREATED="1473763933293" ID="ID_1457528766" MODIFIED="1473763933993" TEXT="nether">
<node CREATED="1473763752189" ID="ID_1557638101" MODIFIED="1473763773017" TEXT="when dying in nether, with souls left, souls should drop and player transported back to world">
<node CREATED="1473763775173" ID="ID_1708841129" MODIFIED="1473763830201" TEXT="All souls in inventory should drop">
<node CREATED="1473763831285" ID="ID_1233429313" MODIFIED="1473763902668">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      People may decide to visit nether with only one soul in inventory.&#160;So if that player drops no souls, killing them has no value (and we want killing people in the nether to be valuable)
    </p>
  </body>
</html></richcontent>
</node>
</node>
</node>
</node>
<node CREATED="1473764906573" ID="ID_1331054879" MODIFIED="1473764908289" TEXT="world reuse">
<node CREATED="1473764909365" ID="ID_75196820" MODIFIED="1473764923689" TEXT="abandonment">
<node CREATED="1473764925061" ID="ID_829875226" MODIFIED="1473764933625" TEXT="Occurs when all of the following">
<node CREATED="1473764934670" ID="ID_974322471" MODIFIED="1473764944138" TEXT="no portals exist to other worlds"/>
</node>
</node>
</node>
<node CREATED="1473763557829" ID="ID_760795941" MODIFIED="1473763559425" TEXT="manual">
<node CREATED="1473763560637" ID="ID_508662675" MODIFIED="1473763571017" TEXT="Subjects">
<node CREATED="1473764026101" ID="ID_1519681681" MODIFIED="1473764027658" TEXT="pvp">
<node CREATED="1473764028773" ID="ID_1506918180" MODIFIED="1473764031473" TEXT="on, always"/>
<node CREATED="1473764031845" ID="ID_1837632303" MODIFIED="1473764043985" TEXT="find a world with nice people, or create your own and only allow nice people"/>
</node>
<node CREATED="1473763572165" ID="ID_1498478071" MODIFIED="1473763582745" TEXT="semi-perma death">
<node CREATED="1473763631141" ID="ID_1111254238" MODIFIED="1473763632625" TEXT="souls"/>
</node>
<node CREATED="1473763636541" ID="ID_713038597" MODIFIED="1473763638161" TEXT="nether">
<node CREATED="1473763640949" ID="ID_412189619" MODIFIED="1473763650769" TEXT="souls drop when player dies"/>
</node>
<node CREATED="1473763583013" ID="ID_1767177315" MODIFIED="1473763584465" TEXT="griefers">
<node CREATED="1473763964005" ID="ID_748733712" MODIFIED="1473763977139" TEXT="kill the griefer and he can probably never come back"/>
<node CREATED="1473763977733" ID="ID_100214371" MODIFIED="1473764012137" TEXT="die and the griefer can chase you, so either kill him, hide or hope he stays in the same living world and doesn&apos;t follow you"/>
</node>
<node CREATED="1473763584901" ID="ID_214638953" MODIFIED="1473763611956" TEXT="portals">
<node CREATED="1473763624846" ID="ID_555212777" MODIFIED="1473763628889" TEXT="portal keys"/>
<node CREATED="1473763620989" ID="ID_229375727" MODIFIED="1473763622857" TEXT="to nether"/>
<node CREATED="1473763591949" ID="ID_362344784" MODIFIED="1473763593721" TEXT="from nether"/>
<node CREATED="1473763594197" ID="ID_435025403" MODIFIED="1473763599633" TEXT="multiple portals to nether"/>
<node CREATED="1473763600572" ID="ID_1630882486" MODIFIED="1473763604569" TEXT="destroying portals"/>
</node>
<node CREATED="1473764057069" ID="ID_714860943" MODIFIED="1473764060257" TEXT="ancient worlds">
<node CREATED="1473764061309" ID="ID_1241853049" MODIFIED="1473764074993" TEXT="will be revisitable after abandoned"/>
</node>
</node>
</node>
<node CREATED="1473763489445" ID="ID_673670273" MODIFIED="1473763490433" TEXT="testing">
<node CREATED="1473763491637" ID="ID_92793187" MODIFIED="1473763526865" TEXT="most populated world gets spawned in first"/>
</node>
</node>
</node>
</node>
</map>
