Group Leopard project documentation
Group member: Yuewei Tian and Junshang Tian

1.	Project Description
This project is a space colony crew management and mission simulation system developed based on the Android platform. Players can create crew members of different professions, manage their training, dispatch them on missions, combat various randomly generated threats, and collect relevant data on mission execution.
The main player workflow includes:
a.	Recruiting Crew Members (Different Professions)
b.	Managing Crew Member Status (Level Up, Energy, Experience, etc.)
c.	Selecting Crew Members for Missions
d.	Engaging in Turn-Based Combat During Missions
e.	Gaining Experience and Improving Crew Member Abilities
This system features:
a.	Multi-Profession System (Each profession has different abilities)
b.	Turn-Based Combat Mechanism
c.	Experience Growth System
d.	Buff/Debuff Mechanism
e.	Local Data Storage (Supports Saving and Loading)
f.	Complete Game Flow (Creation → Missions → Growth)

2.	Implementation
2.1. Technical Environment
This project uses the following technologies:
Programming Language: Java
Development Platform: Android Studio
Data Storage: Gson (JSON format)
UI Components: RecyclerView, Activity
2.2. Structure
The system follows a layered architecture design, consisting of four main layers: UI, Manager, Game Logic, and Data Storage.
The UI layer handles user interaction and display. The Manager layer, including GameManager and MissionControl, coordinates game flow and logic. The Game Logic layer defines core gameplay elements such as CrewMember, Threat, and combat mechanics. The Data layer manages persistent storage using JSON serialization.
2.3. Core Module Description
(1) CrewMember (Crew System)
This is the core class of the entire system, used to represent crew members.
Main attributes include name, specialization, skill level, energy, and resilience.
The main functions include: taking damage (takeDamage), defending (defend), gaining experience (getExperience), and using special skills (special).
(2) Character System
The system features five characters: Pilot (high defense), Engineer (balanced), Medic (healing ability), Scientist (high skill), and Soldier (high attack).
Each character inherits from CrewManager and has their own set of skills; some characters also have special bonuses when facing specific threats.
(3) MissionControl (Mission System)
This class is responsible for controlling the entire combat flow and is the core logic of the game.
Functions include: generating enemies (Threats), controlling turn order, handling player actions (attack, defense, skills), calculating damage, and determining victory or defeat.
Combat Mechanics (Turn-based)
Each Turn:
Player Chooses Action: Attack, Defend, Special (Skill)
Damage Calculation: Damage = Attack Power - Defense Power
Enemy Counterattacks
And a textual description of the battle process.
(4) Threat System
Represents enemies or events in a mission.
Attributes: Health, Skill, Resilience, Type
Different Threats have different effects, for example:
Virus Outbreak: Crew members lose energy
System Failure: Defense decreases
New Planet: Experience increases
Alien Invasion: Mass attack
(5) Storage (Data Storage)
Used to save game data.
Functions: Saves crew information, reads save files, and manages all crew members.
Data is converted to JSON files using Gson and saved.
(6) GameManager (Global Control)
Uses Singleton Pattern:There is only one GameManager in the entire game. 
Its functions include: managing Storage, managing MissionControl, and providing a unified entry point.
(7) MedBay (Medical System)
Used for managing injured crew members.
Functions: Store injured crew members, restore crew member status, send crew members back to Quarters.

3.	UML Diagram
This UML diagram shows the structure of the system. CrewMember is an abstract class with multiple subclasses. MissionControl manages the mission logic. Storage handles data persistence.

4.	Application Workflow
1. First, the user opens the application and enters the main interface.
2. Select and create crew members. Enter crew member names, select professions, and click Create. New crew members will appear in Quarters.
3. Manage crew members: In Quarters, you can view crew member status and statistics (details). Select crew members for training or missions.
4. Execute missions: Select 2-3 crew members and click "Go to Mission." You will be taken to the mission interface. Click "Launch Mission," and the system will generate a Threat.
5. Combat flow: Each round, players have free control (choose Attack / Defend / Special). The system calculates damage, and then Threats counterattack.
6. Combat results: In a victory, surviving crew members gain experience. In a defeat, all crew members enter MedBay.
7. MedBay: Crew members in MedBay can be sent back to the quarter in the quarter interface. Returning crew members recover energy, but their experience is reset to zero (as a penalty).
8. Training system: Crew members can move from the quarter to the training room for training. Trained crew members gain experience, which can be converted into other attributes when sufficient experience is accumulated.
9. Data Management: In the quarter interface, clicking "save" saves the created crew members and their data descriptions, while clicking "load" restores the last saved crew member.
10. Statistics: The statistics interface allows you to view combat data.

5. Installation 
This project need to download dependencies first.
Steps: Open the project in Android Studio and click "Sync Now" to download dependencies if your computer does not sync automatically and click Run to run it on your emulator or phone.

6. GitHub and video link locations
The GitHub and video links are included in the online text file of your assignment submission, and can also be found in the report.
GitHub: https://github.com/wei10101/spacecolonygame.git
Video: spacecolonygame – MissionControl.java [spacecolonygame.app.main] 2026-04-19 13-44-22.mp4

7. Team members
Yuewei Tian 002689458
Junshang Tian 003313192

8. Collaboration 
Yuewei Tian is responsible for core logic development, including game rules and data structures. Key modules include, but are not limited to, CrewMember + all subclasses (Pilot / Engineer / ...), Threat, MissionControl, MedBay, Buff / Debuff system, and combat calculations (damage, turns).
Junshang Tian is responsible for UI and system integration, including interface and user experience. Key modules include, but are not limited to, RecruitActivity, QuartersActivity, MissionControlActivity, CrewAdapter (RecyclerView), and button logic (Attack / Defend / Special).
Use GitHub to share code and implement version control.

9. Tools
Android Studio: Development
Java: Programming
Gson: Data Storage
GitHub: Code Management
PlantUML: Drawing UML Diagrams

10. Basic Functionality
The application includes the following features:
1. Crew Management
i. Users can create different types of crew members (Pilot, Engineer, Medic,
Scientist, Soldier).
ii. Newly recruited crew members are placed in Quarters.
iii. Users can move crew members to the Simulator or Mission Control.
2.Training System
iv. Crew members gain 1 experience points when trained in the Simulator, but reduces energy by 5. This avoids numerical inflation caused by unlimited training.
v. Experience increases skill power. For every 5 experience points gained, skill increases by 2.
3. Cooperative Mission System
vi. Users can select two or three crew members to go on a mission.
vii. The system will randomly generate threats with different attributes, and some threats will even contain special abilities.
viii. Missions follow a turn-based system:
• Crew members take turns acting against the threat.
• The threat retaliates after each crew member’s action.
• The mission continues until the threat’s energy drops to zero or all crew members are defeated.
• Surviving crew members gain 2 experience points.
• Fallen crew members will be taken to Medbay for treatment, and will return to the quarter with restored energy but zero experience.
4.Crew Recovery
ix. When a crew member returns to Quarters, its energy is fully restored but experience points remain.
Object-Oriented Programming – Project Work
5.Data Structures
p. The program use data structures effectively.
q. HashMap<Integer, CrewMember> is used for storing crew members and their IDs.
r. ArrayList is widely used in the system to manage dynamic collections of crew members. It is used in MissionControl to store the selected team, in MedBay to track injured crew, in Storage to return filtered crew lists, and in the UI adapter to handle user selections.

11. Bonus Features
1. RecyclerView	
The application uses RecyclerView to efficiently display and manage a list of crew members. Data is retrieved from the Storage class and passed to a custom adapter (CrewAdapter), which binds crew information such as name, specialization, skill level, and energy to the UI components.
The RecyclerView supports user interaction, including selecting up to three crew members, highlighting selected items, and navigating to a detailed view of each crew member.
2. Crew Images	
Each crew specialization is represented with a unique image.
3. Mission Visualization	
Use a progress bar (energy bar) in the mission interface to display the energy changes of the threat and the crew.
4. Tactical Combat	
Implements a turn-based combat system where players can choose actions such as attack, defend, or use special abilities, while threats respond automatically.
Defensive effect: Medic: Grants 5 energy to the crew with the lowest health；
others ：halves the damage taken.
Each crew specialization has a unique special ability:
Medic: Restores 5 energy to all crew members. 
Engineer: Gains +1 defense for one turn. 
Soldier: Deals high burst damage (Damage increased by 3 and ignores defense). 
Scientist: Reduces the threat’s defense by 2. 
Pilot: Causes the threat to skip its next attack.

5. Statistics	
Tracks game data including missions completed, victories, training sessions and statistics on individual crew members.
6. No Death System	
Instead of permanent death, defeated crew members are sent to the MedBay and can later recover, maintaining game continuity.
7. Randomness in Missions	
Introducing randomness into combat and mission generation, such as changing threat damage and crew damage (ranging from +2 to -2), randomizing the threat type for each mission, and dealing damage to random crew members each turn, can enhance the game's replayability.
8. Specialization Bonuses	
Crew members of different professions may receive specific skill bonuses when facing certain threats, including engineer receiving a +2 skill bonus when facing system failures; 
scientist receiving a +2 skill bonus when facing new planets;  medic receiving a +2 skill bonus when facing virus outbreaks.
9. Larger Squads	
Supports missions with up to three crew members.
10. Data Storage & Loading	
The game uses Gson to save and load data from a JSON file. 
Since CrewMember is an abstract class with multiple subclasses, a RuntimeTypeAdapterFactory is used to preserve subclass type information during serialization and deserialization. The save and load buttons are located in the quarter interface.
11. Custom Feature: 
Threat Effects	Add unique threat effects; some threats will produce certain effects at the start of the mission, such as: System Failure: All crew defense is reduced by 1; Virus Outbreak: Crew energy is halved at the start; New Planet: Experience gained upon mission completion is increased by 1.
12. Custom Feature:
Difficulty Level Mechanism	The difficultyLevel attribute is used to dynamically scale the strength of threats based on the player's progress. It is calculated using both the number of selected crew members and the total number of missions completed.
This value directly affects key attributes of the threat, such as health and attack power, ensuring that the game becomes progressively more challenging over time.
