## Cryptocurrency Application

### reference : https://www.youtube.com/watch?v=EF33KmyprEQ

### APIs for cryptocurrencies
  * https://api.coinpaprika.com/v1/coins
  * https://api.coinpaprika.com/v1/coins/btc-bitcoin
  
### Technologies
  * Clean Architecture using MVVM and Use Cases
    * Clean Architecture consists of **four layers**
      * Model
      * View
      * ViewModel
        * in original MVVM, all the codes about business logic is put into ViewModels
        -> The bigger the project gets, the more needs to get a viewModel that does everything
      * Use Cases
        * a part of feature of the application
          * feature : a set of screens related together
          * use case : single action we can do in features
        * ex) Profile Use Cases
          * 'Getting' User Profile Data = single action -> single feature
          * 'Updating' User Profile Pic = single action -> single feature
          * ...
        * don't need to be network calls or database queries
          * ex) searching in a list can also be one of the use cases...
        * Advantages of Use Cases
          * Reusable
            * Get Profile Data Use Case can be needed on 'multiple places' in 'different viewmodels'
            * if not managed with a use case -> logic must be put each viewmodel... for multiple times
            * Removes Code Duplication
          * Leads to Screaming Architecture(Martin)
            ![images_pond1029_post_ebafcd39-6659-41e0-821b-0d96bbe7c80b_image.png](..%2F..%2F..%2FPCIS-1%7E1%2FAppData%2FLocal%2FTemp%2Fimages_pond1029_post_ebafcd39-6659-41e0-821b-0d96bbe7c80b_image.png)
            * package screams what it is used for
            
    * Separating the project into **three layers**
      * presentation -> ui
        * present sth to the user
        * put all the composables
      * domain
        * contain models
        * entities containing data
        * repositories + interfaces for repositories
        * business logic(-> use cases)
      * data
        * the name of the data "says" what kind of data it contains
        * API interfaces
        * database
        * repositories

---

### Package Structure
  * data
    * repository
    * remote
      * for remote data source
    * dto
      * Data Transfer Object
      * the object that api returns
  * domain
    * model
    * repository
    * use_case
      * get_coins => to get data about coins
      * get_coin => to get a single coin data
  * presentation
    * components : components that all the screens share
    * every single screen, another package
      * coin_list
        * components
      * coin_detail
        * components
  * common
    * all the codes other three layers have in common
    * ex) file for Constants, Resources
      * Resource Sealed Class
  * di(dependency injection)

  > cf) dto of model vs model of domain
    dto는 네트워크 요청으로 가져오는 날것의 데이터 틀이고, model은 ui에 보여줄 단위대로 객체를 짜놓은 것을 이름
    dto로 가져오는 모든 데이터를 ui에 띄우고 싶지 않을 수 있음. 그 때 사용하는 것이 바로 model
    very often, don't need any api return
    so when needed a lighter model that only contain the data that are **shown in the ui**
    that lighter models come into the domain > model package.
    data > remote > dto : data **that come directly from the api**

---
***top -> down approach***
-> implement ui last...
---

### implementing the API & Repositories

* Interface : CoinPaprikaApi.kt
  * data > remote
  * retrofit api interface
    * getCoins
    * getCoinById
  * define functions
  * return type object -> data > remote > dto
* Data Class => for getting raw data (가져오는 것)
  * data > remote > dto
  * CoinDto.kt
  * CoinDetailDto.kt 외 여러 데이터 클래스들
* Data Class => for displaying (실사용 하는 것)
  * domain > model
  * not all the data from dto are not needed to show in the ui
    * Coin.kt
      * compared to CoinDto.kt, is_new, type is excluded...
    * CoinDetail.kt
  * 가져오는 것과 실제로 사용하는 것이 다르다. 따라서, 이 둘 사이를 map해주는 것이 필요
    * to transform a CoinDto(one Object) into a Coin(another Object)
    * CoinDto.kt > fun CoinDto.toCoin(): Coin
* Repository Class
  * domain > repository > repository vs data > remote > repository
    * in the domain layer, we can...
      * define repositories
        * CoinRepository.kt(Interface class)
      * define the function => the similar functions we actually have in api function
        * one function for each api request!!!
          * getCoins
          * getCoinById
    * in the data layer, we can...
      * contain 'implementation' of the repository => actual implementations of the interface(defined in domain layer)
      * reason : can directly access to the data(data > remote > dto...)
      * Dependency Injection : inject dependencies
        * api 인터페이스 주입(네트워크 통신을 위함)
  * Looks like boilerplate code... but the bigger the project gets, the more helpful...
  * By doing so, you can **use one repository for multiple times**(usecases, viewmodels...)

---

### implementing use cases

* use repositories to access the api + forward the information to the viewmodels

* The Principle of Use Cases
  * Each Use Case should only have One Public Function!
    * the function to execute that use case

* domain > use_case
  * name convention : DoWhat + UseCase
  * get_coin
    * GetCoinsUseCase.kt(Class) -> to get the coin... -> operator function 이용
      * inject CoinRepository(Interface), **not CoinRepositoryImpl**
        * reason : injecting an interface is more replaceable
      * override the **invoke operator** function => to execute the use case
      * UseCase에서는 실제 화면에 적용하기 위한 데이터를 가져오는 것이 목적이지, dto 구성의 데이터를 가져오는 것이 목적이 아님
      * 따라서, 가져오는 데이터를 Coin으로 해야함
        > operator fun invoke(coinId: String): Flow<Resource<List<Coin>>> = flow {
          }
  * get_coins
    * 같은 과정을 거친다.

---

### setting up dependency injection

* setup with dagger-hilt
  * dependency injection
  * depedency?
    * all the objects in the application
      * database instance
      * api instance
      * glide instance
      * coroutine dispatchers...
  * purpose?
    * must avoid hard coding the dependencies into the objects
    * parameter / constructor로 넘겨주어야지, 클래스 내에서 직접 필요한 객체를 하드코딩해서는 안된다.
      * 재사용성을 해침(bad for reusage)
    * helps us to make dependencies **replaceable**
    * 위의 예시를 토대로 생각해보면, GetCoinsUseCase 클래스에서 만약 다른 정보를 보여주고 싶으면 repository 클래스를 사용해야 하는데,
    * 직접 하드코딩했다면 직접 GetCoinsUseCase 파일료 이동하여 코드를 직접 수정해야 한다. 이는 UseCase 클래스의 재사용성을 해친다.
    * 이는 테스트 시 매우 부정적
    * 따라서, 별도의 모듈을 싱글톤으로 제작하여, 이 모듈로부터 가져다가 쓰는 것이 재사용성에 좋을 것
        

1. di > AppModule.kt => di를 통한 재사용성 제고
  * annotation for AppModule
    * @Module
    * @InstallIn(SingletonComponent::class)
      * how long these dependencies will live
      * SingletonComponent -> dependencies in the module live as long as the application
  * annotation for functions to create dependencies
    * @Provides : means that the function 'provides' dependency
    * @Singleton : only have a single instance of the dependency(that the function returns)
  * define the dependencies to use -> create functions that create the dependencies
    * providePaprikaApi - api instance using retrofit. to provide the api 
    * provideCoinRepository - to provide the repository
      * return the implementation of the repository interface(CoinRepositoryImpl)
        * reason : 이것을 실제로 데이터를 얻기 위해 가져다 쓸 것이기 때문에
  * @InstallIn(SingleComponent::class) != @Singleton
    * 전자는 해당 모듈 안의 dependencies들이 살아있는 기간이 앱이 살아있는 기간동안임을 의미
    * 후자는 function이 return하는 dependency가 앱 전역에서 하나의 인스턴스로 동작하도록 하는 것
  

2. CoinApplication.kt
   * normal class that inherit from Application()
     * purpose : to give dagger-hilt the information about the application
       reason : dagger-hilt should have access to the application context...
     * annotate with @HiltAndroidApp

   * after that, don't forget to set "name" on application tag in AndroidManifest.xml
       <application
           android:name=".CoinApplication"
           ...
       />
---

### setting up the viewModels(each screen)

* ViewModels contain business logic
* and they still technically do 
* but they don't belong to the domain layer, but presentation layer
* because they are directly coupled to the views

* 본래 viewmodel에 있어야 할 비즈니스 로직들을 상당수 use case에 이동시킨 상태.
* 그런데도 viewmodel을 두는 이유?
  * to maintain the "states"
  * keep the state the "ui state"
    * configuration change 발생 시에도 파괴되지 않도록 state를 viewmodel에서 관리 필요
    
* CoinListViewModel.kt
  * annotations
    * @HiltViewModel
    * inject the dependencies with @Inject constructor
      * use case function, not repository
  * things that viewmodel should contain *** 모든 정보를 담는 객체가 필요함에 유의
    * for single screen create 'one' state 'object' that contains 'all' the information
      * 여기서 모든 정보로는 코인 리스트 정보, 프로그레스 정도, 에러 정보 일체
      * 이 정보들을 하나의 객체로서 구현하기 위해, 데이터 클래스 제작
      * CoinListState 데이터 클래스 만들기
    * CoinListState 데이터를 state 변수로 만들기 위하여 다음과 같이 작성
      private val _state = mutableStateOf(CoinListState())
      val state: State<CoinListState> = _state
    * getCoinsUseCase를 이용하여 데이터를 state에 공급하기 위한 function을 다음과 같이 작성
      private val getCoins() {
          getCoinsUseCase() // 함수형태로 사용되지만, 클래스임. 이 안에는 operator fun이 있기 때문에 invoke 함수를 호출하게 되는 것이고, 결국에는 flow를 반환
      }
      // returns the flow that emits the resource values over time
    * onEach 연산자를 통해서 emit하는 것들을 이용

  * CoinListState.kt
    * isLoading => progress를 나타내기 위함
    * coins => 로딩 후 띄우기 위한 데이터(list of coins)
    * error => potential error
      
* CoinDetailViewModel.kt
  * CoinListViewModel과 다른 점
    * GetCoinUseCase에 있는 Flow 변수를 얻기 위하여, invoke 함수에 coinId 인자가 필요함.
    * 이 인자는 어떻게 얻어서 넣는 것이 좋은가?
      * SavedStateHandle을 이용
        * SavedStateHandle의 인스턴스를 ViewModel 클래스에 주입
        * a bundle that contains information about the saved 'state'
        * 이것을 사용하여, 예상치 못한 오류 발생에도 앱을 복구할 수 있음
        * 또한, 이것은 navigation parameter 역시 보존할 수 있음

---

### Issues During Project

* https://salmonpack.tistory.com/34
* https://developer.android.com/training/dependency-injection/hilt-android?hl=ko