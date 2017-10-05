# TransitionAnimation

### 설명
____________________________________________________

![TransitionAnimation](https://github.com/Hooooong/DAY21_Transition_Animation/blob/master/image/contactAnimation.gif)

- Transition Animation

- Activity 이동 간 공유 요소를 설정하여 Activity 전환을 한다.

### KeyPoint
____________________________________________________

- Transition Animation 란?

  - 머티리얼 테마에서 버튼 및 액티비티 전환을 위한 애니메이션을 제공하는데, Android 5.0(LOLLIPOP) 버전에서는 이러한 애니메이션을 사용자가 지정하거나, 새로운 애니메이션을 만들 수 있다.

  - 참조 : [사용자 지정 Activity 전환](https://developer.android.com/training/material/animations.html?hl=ko#CurvedMotion)

- 공유 요소 전환

  ![contactAnimation](https://github.com/Hooooong/DAY21_Transition_Animation/blob/master/image/transitionAnimation.gif)

  - A Activity 와 B Activity 간 공유 요소에 대한 View 들을 연결하여 화면 전환을 매끄럽게 하는 Animation 이다.

  - `ContentTransitions`를 style.xml 로 설정하거나, Code 로 설정을 해줘야 한다.

      - xml 설정

      ```xml
      <!-- Base application theme. -->
      <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
          <!-- Customize your theme here. -->
          <item name="colorPrimary">@color/colorPrimary</item>
          <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
          <item name="colorAccent">@color/colorAccent</item>
          <!-- Transition 설정 -->
          <item name="android:windowContentTransitions">true</item>
      </style>
      ```

      - Code 설정

      ```java
      getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
      ```

  - xml 에 transitionName 을 설정한다. 공유 요소들에 대해 동일하게 설정해줘야 한다.

      - list_item.xml

      ```xml
      <TextView
          android:id="@+id/textName"
          <!-- 생략 -->
          android:transitionName="textName"/>
      ```

      - DetailActivity.xml

      ```xml
      <TextView
           android:id="@+id/textName"
           <!-- 생략 -->
           android:transitionName="textName"/>
      ```

  - `ActivityOptions.makeSceneTransitionAnimation()` 를 통해 공유 요소를 설정하고, `Bundle` 로 넘겨주면 된다.

      - 단일 요소 공유

      ```java
      // 단일 요소를 공유하려면
      Intent intent = new Intent(this, DetailActivity.class);
      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), textName, "textName");
      startActivity(intent, options.toBundle());
      ```

      - 다중 요소 공유

      ```java
      // 다중 요소를 공유하려면
      Intent intent = new Intent(this, DetailActivity.class);
      Pair[] pairs = new Pair[2];
      pairs[0] = new Pair<View, String>(textId, "textId");
      pairs[1] = new Pair<View, String>(textName, "textName");

      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);
      startActivity(intent, options.toBundle());
      ```

  ### Code Review
  ____________________________________________________

  - MainActivity.java

    - RecyclerView 설정하고, 임의의 데이터를 설정한다.

    ```java
    public class MainActivity extends AppCompatActivity {

        private RecyclerView recyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            initView();

        }

        private void initView(){
            recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

            CustomAdapter customAdapter = new CustomAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(customAdapter);

            customAdapter.setData(setData());
        }

        private List<String> setData(){
            List<String> data = new ArrayList<>();

            for(int i = 0; i<100; i++){
                data.add(i+"");
            }
            return data;
        }
    }
    ```

- CustomAdapter.java

  - Listener 설정과 공유요소 설정을 한다.

  ```java
  public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

      List<String> data;

      @Override
      public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
          return new Holder(view);
      }

      @Override
      public void onBindViewHolder(Holder holder, int position) {
          String textData = data.get(position);
          holder.setTextId(position);
          holder.setTextName(textData);
      }

      @Override
      public int getItemCount() {
          return data.size();
      }

      public void setData(List<String> data){
          this.data = data;
          notifyDataSetChanged();
      }

      public class Holder extends RecyclerView.ViewHolder {

          private TextView textId, textName;

          public Holder(final View itemView) {
              super(itemView);
              textId = (TextView)itemView.findViewById(R.id.textId);
              textName = (TextView)itemView.findViewById(R.id.textName);

              itemView.setOnClickListener(new View.OnClickListener() {
                  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                  @Override
                  public void onClick(View view) {
                      // 1. Activity Theme 에 정의하는 것
                      // <item name="android:windowContentTransitions">true</item>
                      // 2. Activity.java 에 Code 로 정의하는 것
                      // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                      Pair[] pairs = new Pair[2];
                      pairs[0] = new Pair<View, String>(textId, "textId");
                      pairs[1] = new Pair<View, String>(textName, "textName");

                      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);
                      //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), textName, "textName");

                      Intent intent = new Intent(view.getContext(), DetailActivity.class);
                      intent.putExtra("textId", textId.getText());
                      intent.putExtra("textName", textName.getText());

                      view.getContext().startActivity(intent, options.toBundle());
                  }
              });
          }

          public void setTextId(int position){
              textId.setText(position+"");
          }
          public void setTextName(String data){
              textName.setText("데이터 : " + data);
          }
      }
  }
  ```

- DetailActivity.java

  ```java
  public class DetailActivity extends AppCompatActivity {

      private TextView textId;
      private TextView textName;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          // 2. Activity Code 로 정의
          getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_detail);

          initView();
      }

      private void initView(){
          String id = null;
          String name = null;
          Intent intent = getIntent();
          if(intent != null){
              id = intent.getStringExtra("textId");
              name = intent.getStringExtra("textName");
          }

          textId = (TextView)findViewById(R.id.textId);
          textName = (TextView)findViewById(R.id.textName);

          textId.setText(id);
          textName.setText(name);
      }
  }
  ```

- activity_main.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.hooooong.transitionanimation.MainActivity">

      <android.support.v7.widget.RecyclerView
          android:id="@+id/recyclerView"
          android:layout_width="0dp"
          android:layout_height="0dp"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintRight_toRightOf="parent"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintBottom_toBottomOf="parent"/>

  </android.support.constraint.ConstraintLayout>
  ```

- list_item.xml

  - RecyclerView 에 보여줄 item 에 대한 xml 이다.

  - `android:transitionName` 를 설정해준다.

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="50dp"
      android:orientation="horizontal">

      <TextView
          android:id="@+id/textId"
          android:layout_width="wrap_content"
          android:layout_height="match_parent"
          android:layout_gravity="center"
          android:gravity="center"
          android:layout_weight="0.10"
          android:transitionName="textId"/>

      <TextView
          android:id="@+id/textName"
          android:layout_width="wrap_content"
          android:layout_height="match_parent"
          android:layout_gravity="center"
          android:layout_weight="1"
          android:gravity="left|center"
          android:paddingLeft="10dp"
          android:transitionName="textName"/>
  </LinearLayout>
  ```

- activity_detail.xml

  - `android:transitionName` 를 설정해준다.

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.hooooong.transitionanimation.DetailActivity">

      <TextView
          android:id="@+id/textId"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          app:layout_constraintTop_toTopOf="parent"
          android:layout_marginTop="8dp"
          android:layout_marginRight="8dp"
          app:layout_constraintRight_toRightOf="parent"
          android:layout_marginLeft="8dp"
          app:layout_constraintLeft_toLeftOf="parent"
          android:layout_marginBottom="8dp"
          app:layout_constraintVertical_bias="0.822"
          app:layout_constraintBottom_toTopOf="@+id/textName"
          android:transitionName="textId"/>

      <TextView
          android:id="@+id/textName"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="TextView"
          android:layout_marginRight="8dp"
          app:layout_constraintRight_toRightOf="parent"
          android:layout_marginLeft="8dp"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintBottom_toBottomOf="parent"
          android:layout_marginBottom="220dp"
          android:transitionName="textName"/>
  </android.support.constraint.ConstraintLayout>
  ``
