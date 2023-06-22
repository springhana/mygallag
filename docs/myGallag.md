## myGallage 분석
* 플로우차트

<br/>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/39dc3391-26fe-4e09-a2d5-11b347d92dbe"/>
</p>
<hr/>

# StartActivity 
<br/>
<h4>※ 게임시작 전의 대기 화면이며 플레이어 캐릭터를 선택할 수 있고 캐릭터 선택 시 나오는 가운데의 원(게임시작 버튼)을 클릭하면 선택한 캐릭터 정보를 MainActivity에 넘기며 게임을 시작한다.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/28572c02-5b79-49cd-abc2-ec055a491951"/>
</p>
<hr/>

# MainActivity 
<br/>
<h4>※ 게임 진행중인 화면으로 UI와 배경음악, 효과음을 구현하고 있으며 게임화면은 SurfaceView를 사용한 SpaceInverdersView에 Context를 넘겨서 구현한다.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/796bd5cc-1747-48a1-ae2a-510e550f72b4"/>
</p>
<hr/>

<br/>
<h4>※ 게임화면을 구현하는 View를 생성하고, LinearLayout인 gameFrame에 추가한다. View 생성시 MainActivity의 Context와, 앞에서 선택한 플레이어 캐릭터의 정보들을 함께 넘겨준다.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/d91c4ab6-3987-4088-b108-2e0da8f1e1ad"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/1fa367ce-8af0-43d2-a898-5aa08c62c125"/>
</p>
<hr/>

<br/>
<h4>※ 조이스틱을 움직일 때 마다 조이스틱의 방향과 가해지는 힘에 따라 플레이어 캐릭터의 움직임을 조정한다.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/bbc09c4e-58f7-403e-8f20-2f5e13b053a0"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/f253fc4e-689f-424a-be09-c7e98438a914"/>
</p>

<h4>※ 해당 버튼을 누를 때마다 게임 뷰의 총알 발사 함수를 실행.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/5a87d2c8-d045-4ce1-aab2-f094c7dd5efa"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/fc0b7670-ff8e-4764-b34e-6394b89020f3"/>
</p>

<h4>※ 해당 버튼을 누를 때마다 게임 뷰의 총알 재장전 함수를 실행.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/d9926e8a-c059-4553-a4c3-ef438b555e8d"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/21982ded-aac0-4d2b-930c-b8449d34aa51"/>
</p>

<h4>※ 게임 뷰의 플레이어의 특수 공격 함수 실행.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/b28ea42c-ba42-42ab-bddf-9c55dd4401be"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/0f79ddde-511a-4904-bcd9-f296ce9112bf"/>
</p>

<h4>※ 게임 뷰의 일시 정지 함수를 실행.</h4>
<h4>※ 일시정지용 레이아웃 PauseDialog을 화면에 띄움.</h4>
<p>
  <img src="https://github.com/springhana/mygallag/assets/97121074/504c09dc-c436-46a2-8812-0601796ac7f7"/>
  <img src="https://github.com/springhana/mygallag/assets/97121074/517c5ede-0baa-4621-8464-c50f1e30661d"/>
</p>
<hr/>

# PauseDialog 
<br/>
<h4>※ 게임 일시정지시 나타나는 레이아웃이며, 배경음악과 효과음을 켜고 끌 수 있다.</h4>
<h4>※ 확인버튼이나, 우측 상단의 X버튼을 누르면 Dismiss 함수를 실행해, 다시 게임 화면으로 돌아간다.</h4>
<img src="https://github.com/springhana/mygallag/assets/97121074/de4c6cdc-bf40-4eb9-8aaa-92c2869c1ddd"/>
<hr/>

# ResultActivity 
<br/>
<h4>※ 게임 종료시 나타나는 레이아웃이며, SpaceInverdersView에서 점수를 넘겨받아 최종 점수를 표기해준다.</h4>
<h4>※ 처음으로 버튼을 클릭하여 맨 처음 화면인 StartActivitiy로 넘어간다.</h4>
<img src="https://github.com/springhana/mygallag/assets/97121074/b53be53b-dc07-4ec9-9262-631a062afe0e"/>
<hr/>

# SpaceInverdersView 
<br/>
<h4>※ MainActivity에서 게임이 진행되는 화면을 SurfaceView로 생성한 View.</h4>
<h4>※ SurfaceView는 더블 버퍼링 방식으로 화면을 구현하기 때문에 게임화면 구현에 알맞음.</h4>
<h4>※ 한 프레임 마다 화면에 존재하는 모든 객체의 위치를 조정하고, 객체끼리의 충돌 처리를 진행해 화면에 그려 내는 작업을 해준다.</h4>
<h4>※ 게임 종료시 ResultActivity를 열면서 점수를 넘겨준다.</h4>
<img src="https://github.com/springhana/mygallag/assets/97121074/82aacab8-accb-42fd-bd2a-223037bc0621"/>
<hr/>

# Sprite 
<br/>
<h4>※ SpaceInverdersView에 나타나는 모든 객체의 이미지 정보를 저장하는 클래스 각 객체들은 Sprite를 상속받는다.</h4>
<h4>※ 이미지의 좌표, 가로 세로 크기, 움직이는 크기, 이미지 파일, 이미지 리소스 파일 ID, 충돌처리를 위한 이미지 각 꼭짓점의 좌표를 인스턴스로 갖는다.</h4>
<hr/>

# StartshipSprite 
<br/>
<h4>※ 플레이어 캐릭터의 각종 정보를 저장하고 기능을 구현하는 클래스 플레이어의 현재 속도, 총알 개수, 생명 개수, 파워 레벨, 특수 공격 가능 횟수 등을 인스턴스로 가진다.</h4>
<h4>※ 구현하는 기능으로는 플레이어의 움직임, 총알 발사, 총알 재장전, 특수 공격 실행, 다른 객체와의 충돌을 체크해 어떤 객체와 충돌 했느냐에 따라서 여러 기능을 수행한다.</h4>
<h4>※ 적 또는 적의 총알과 충돌했을 시 생명을 한개 잃고 각종 아이템들과 충돌 했을 시 그 아이템에 맞는 기증을 수행한.</h4>
<hr/>

# AlienSprite 
<br/>
<h4>※ 적 객체의 각종 정보를 저장하고 기능을 구현하는 클래스.</h4>
<h4>※ 적 생성시 적이 움직이는 방향과 속도는 랜덤으로 정해지며, 1초마다 30퍼센트의 확률로 적이 총알을 발사한다.</h4>
<h4>※ 충돌 처리를 통해 플레이어의 총알 또는 특수공격과 충돌하면 파과되며, 파괴시 확률에 따라 각종 아이템을 생성한.</h4>
<hr/>

# ShotSprite 
<br/>
<h4>※ StartshipSprite에서 총알 발사 함수가 실행 될 때마다 생성된다.</h4>
<hr/>

# SpecialshotSprite 
<br/>
<h4>※ StartshipSprite에서 특수공격함수가 실행될 때마다 생성된다.</h4>
<h4>※ 생성시(특수공격 사용시) 5초 동안 화면에 나와있으며 그 5초동안 총알 발사와 특수 공격을 사용 못하게 막는다.</h4>
<hr/>

# AlienShotSprite 
<br/>
<h4>※ AlienSprite에서 총알 발사 함수가 실행 될 때마다 생성된다.</h4>
<hr/>

# ItemSprite 
<br/>
<h4>※ 게임에서 적 객체 파괴시 랜덤으로 맵에 아이템들이 나타나며 각 아이템은 생성 후 10 ~ 15초가 지나면 맵에서 사라진다.</h4>
<h4>※ Heal, Power, Speed, SpecialShot 이 있다다.</h4>

![special_item](https://github.com/springhana/mygallag/assets/97121074/077cb26a-d5fd-4558-bbc2-4aa154eb3207)
![speed_item](https://github.com/springhana/mygallag/assets/97121074/74451a62-1d71-4d67-acf7-3d6683613a44)
![heal_item](https://github.com/springhana/mygallag/assets/97121074/eb79caf0-8dad-453d-ac33-2e00c6a62f6e)
![power_item](https://github.com/springhana/mygallag/assets/97121074/9d047e56-5503-4e28-84a4-23d7c0c71aaa)

<hr/>
