# Fontend 소개
## 사용방법
Atomic Design
 웹 프론트 개발 프레임워크, 라이브러리인 Angular, Vue, React는 컴포넌트 단위로 개발을 진행한다.
이러한 컴포넌트를 가장 작은 단위로 설정하고 이를 바탕으로 상위 컴포넌트를 만들어 코드 재사용을 최대화하는 방법론이다. 따라서 아토믹 디자인이 컴포넌트 중심 설계 패턴에서 더욱 주목받게 되었다.
 
아토믹 디자인은 원자(Atoms), 분자(Molecules), 유기체(Organisms), 템플릿(Templates), 페이지(Pages)로  효과적인 인터페이스 시스템을 만든다.

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FvoVIZ%2FbtrzDPi16ML%2F3HcNnUSQKFutl8DnMY0Zu1%2Fimg.png">
<br>
<h3>Atoms(원자)</h3>
<ul>
<li>가장 작은 단위의 컴포넌트이다. (디자인과 기능의 최소 단위)
<li>다양한 state 즉 상태, 색상, 폰트, 애니메이션과 같은 추상적인 요소가 포함될 수 있다.
<li>대표적인 컴포넌트는 버튼(Button), 텍스트(Text), 아이콘(Icon) 등이 있다.
</ul>

<h3>분자(Molecules)</h3>
<ul>
<li>2개 이상의 원자로 구성되어 있다. 
<li>하나의 단위로 함께 동작하는 UI 컴포넌트들의 단순한 그룹이다.
<li>대표적인 컴포넌트는 입력 폼(Input Form), 내비게이션(Navigation), 카드(Card) 등이 있다.
</ul>

<h3>Organism(유기체)</h3>
<ul>
<li>분자들을 결합하여 유기체를 형성 (분자가 되지 않은 원자도 포함)
<li>인터페이스가 어떻게 보이는지 시작하는 단계
<li>대표적인 컴포넌트는 입력 폼과 함께 헤더를 감싸거나, 카드를 관리하는 그리드 등이 있다.
</ul>

<h3>템플릿(Templetes)</h3>

<ul>
<li>여러 유기체의 집합
<li>디자인을 확인하고 레이아웃이 실제로 구동하는지 확인하는 단계
<li>대표적인 컴포넌트는 여러 카드와 그리드를 묶는 템플릿(헤더, 메인, 푸터) 등이 있다.
</ul>

<h3>Page(페이지)</h3>

<ul>
<li>템플릿을 이용해서 배치를 통해 <li>컴포넌트를 그려서 디스플레이한다.
완성된 하나의 페이지이다.
</ul>

