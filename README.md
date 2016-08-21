# Multi Type RecyclerView Adapter
A simple adapter for creating a RecyclerView with an unlimited number of view types.

# Usage
**Declar your view types**
```java
public enum DemoViewTypes implements ViewType {

    VIEW_TYPE_ONE, VIEW_TYPE_TWO, FOOTER;

    @Override
    public int getType() {
        return ordinal();
    }
}
```
An Enum is a good choice for this

**Create a class to hold your view type**
```java
public class ViewTypeOne {
    
}
```
**Add a MultiTypeCreator, MultiTypeBinder, ViewHolder as static inner classes**
```java
public class ViewTypeOne {

    public static class Creator implements MultiTypeCreator {

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.VIEW_TYPE_TWO;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_type_two, parent, false);
            return new ViewHolder(view);
        }
    }

    public static class Binder implements MultiTypeBinder {

        private final String mData;

        public Binder(String data) {
            this.mData = data;
        }

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.VIEW_TYPE_TWO;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder) {
            final ViewHolder typeOneHolder = (ViewHolder) holder;
            typeOneHolder.mText.setText(mData);
        }
    }

    public static class ViewHolder extends SimpleMultiTypeViewHolder {

        private final TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.data);
        }
    }
}
```
The `MultiTypeCreator` is responsible for inflating the view used for this view type and is the equivilant of `createViewHolder(ViewGroup parent, int viewType)` that you would normally override. The `MultiTypeBinder` is resposible for binding data to the view and is the equivelant of `bindViewHolder(VH holder, int position)`. The `ViewHolder` is just a regular view holder apart from you need to implement `MultiTypeViewHolder` or extend `SimpleMultiTypeViewHolder`

**Use the MultiTypeAdapter**
```java
mAdapter = new MultiTypeAdapter.Builder()
    .addCreator(new ViewTypeOne.Creator())
    .addCreator(new ViewTypeTwo.Creator())
    .addCreator(new FooterViewType.Creator())
    .build();
```
You need to add all of the `MultiTypeCreator` you want to use with the adapter

```java
Binder binder = ViewTypeOne.Binder("View type one");
mAdapter.add(binder)
```
Add your binders to the adapter
