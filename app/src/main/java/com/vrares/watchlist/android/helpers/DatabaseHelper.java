package com.vrares.watchlist.android.helpers;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vrares.watchlist.R;
import com.vrares.watchlist.models.adapters.FriendRequestPendingAdapter;
import com.vrares.watchlist.models.adapters.FriendRequestPendingAdapterCallback;
import com.vrares.watchlist.models.adapters.MovieListAdapterCallback;
import com.vrares.watchlist.models.adapters.MovieListAdapter;
import com.vrares.watchlist.models.adapters.UserListAdapter;
import com.vrares.watchlist.models.adapters.UserListAdapterCallback;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.pojos.Watcher;
import com.vrares.watchlist.presenters.callbacks.FavListPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.FriendsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.HitListPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.MovieDetailsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.UserDetailsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.UserProfilePresenterCallback;
import com.vrares.watchlist.presenters.callbacks.UserSearchPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.WatchersPresenterCallback;
import com.vrares.watchlist.presenters.classes.FavListPresenter;
import com.vrares.watchlist.presenters.classes.FriendsPresenter;
import com.vrares.watchlist.presenters.classes.MovieDetailsPresenter;
import com.vrares.watchlist.presenters.classes.UserProfilePresenter;
import com.vrares.watchlist.presenters.classes.WatchersPresenter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Singleton;

@Singleton
public class DatabaseHelper {

    private static final String USERS_NODE = "users";
    private static final String USER_PICTURE_NODE = "picture";
    private static final String MOVIES_NODE = "movies";
    private static final String MOVIE_NODE = "movie";
    private static final String HIT_LISTS_NODE = "hitList";
    private static final String FAV_LISTS_NODE = "favList";
    private static final String SEEN_BY_NODE = "seenBy";
    private static final String SEEN_COUNT = "seenCount";
    private static final String FULL_NAME_NODE = "fullname";
    private static final String TIME = "time";
    private static final String TAG = "Error";
    private static final String EMAIL_NODE = "email";
    private static final String FIRST_NAME_NODE = "firstName";
    private static final String LAST_NAME_NODE = "lastName";
    private static final String PICTURE_NODE = "picture";
    private static final String PROFILE_PICTURES = "profilePictures";
    private static final String JPG = ".jpg";
    private static final String FRIENDS_NODE = "friends";
    private static final String PENDING_FRIENDS_NODE = "pendingFriendRequests";

    private RegisterPresenterCallback registerPresenterCallback;
    private LoginPresenterCallback loginPresenterCallback;
    private MovieListAdapterCallback movieListAdapterCallback;
    private MovieDetailsPresenterCallback movieDetailsPresenterCallback;
    private UserDetailsPresenterCallback userDetailsPresenterCallback;
    private WatchersPresenterCallback watchersPresenterCallback;
    private UserSearchPresenterCallback userSearchPresenterCallback;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    public void insertUserIntoDatabase(final User user, final RegisterPresenterCallback registerCallback, final LoginPresenterCallback loginCallback, final UserDetailsPresenterCallback userDetailsCallback) {
        this.registerPresenterCallback = registerCallback;
        this.loginPresenterCallback = loginCallback;
        this.userDetailsPresenterCallback = userDetailsCallback;

        databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();

                if (userDetailsPresenterCallback != null) {
                    storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURES).child(auth.getCurrentUser().getUid() + JPG);
                    storageReference.putFile(Uri.parse(user.getPicture()))
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        user.setPicture(task.getResult().getDownloadUrl().toString());
                                        databaseReference.child(auth.getCurrentUser().getUid()).setValue(user);
                                        userDetailsPresenterCallback.onUserInsertedSuccess(user);
                                    } else {
                                        userDetailsPresenterCallback.onUserUpdateFailed(task.getException());
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    userDetailsPresenterCallback.onUserUpdateFailed(e);

                                }
                            });


                } else {

                    if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                        databaseReference.child(auth.getCurrentUser().getUid()).setValue(user);
                    }
                    if (loginPresenterCallback != null) {
                        loginPresenterCallback.onUserInsertedSuccess();
                    } else if (registerPresenterCallback != null) {
                        registerPresenterCallback.onUserInsertedSuccess(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }

    public void getSeenInformation(final Integer movieId, final MovieListAdapter.MyViewHolder holder, final Movie movie) {
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(movieId)).child(SEEN_BY_NODE).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    holder.buttonItem.setBackgroundResource(R.drawable.btn_seen);
                } else {
                    holder.buttonItem.setBackgroundResource(R.drawable.btn_unseen);
                }

                if (!dataSnapshot.hasChild(String.valueOf(movieId))) {
                    holder.detailsItem.setText(movie.getReleaseDate());
                } else {
                    String seenCount = dataSnapshot.child(String.valueOf(movieId)).child(SEEN_COUNT).getValue().toString();
                    holder.detailsItem.setText("There are " + seenCount + " other watchers");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void seenButtonAction(final Movie movie, MovieListAdapterCallback movieListCallback, final MovieListAdapter.MyViewHolder holder, final int position) {
        this.movieListAdapterCallback = movieListCallback;
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();
                setSeen(movie, holder, position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    private void setSeen(final Movie movie, final MovieListAdapter.MyViewHolder holder, final int position) {
        final int[] operations = new int[2];
        final DatabaseReference movieRef = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        final DatabaseReference listsRef = FirebaseDatabase.getInstance().getReference(HIT_LISTS_NODE);
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FULL_NAME_NODE).getValue().toString();
                String userPicture = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(USER_PICTURE_NODE).getValue().toString();
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FULL_NAME_NODE).setValue(userName);
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TIME).setValue(getCurrentDate());
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(USER_PICTURE_NODE).setValue(userPicture);
                listsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(movie.getId())).setValue(movie);
                operations[0] = 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

        movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            int seenCount;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(String.valueOf(movie.getId())) && dataSnapshot.child(String.valueOf(movie.getId())).hasChild(SEEN_COUNT)) {
                    seenCount = dataSnapshot.child(String.valueOf(movie.getId())).child(SEEN_COUNT).getValue(Integer.class);
                    movieRef.child(String.valueOf(movie.getId())).child(SEEN_COUNT).setValue(seenCount + 1);
                } else {
                    movieRef.child(String.valueOf(movie.getId())).child(SEEN_COUNT).setValue(1);
                    seenCount = 1;
                }
                operations[1] = 1;
                if (operations[0] == 1 && operations[1] == 1) {
                    movieListAdapterCallback.onMovieSeen(seenCount, holder, position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getCurrentDate() {
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
    }

    public void unseeButtonAction(Movie movie, MovieListAdapterCallback movieListCallback, final MovieListAdapter.MyViewHolder holder, final int position) {
        this.movieListAdapterCallback = movieListCallback;
        final String movieId = String.valueOf(movie.getId());
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference listDatabase = FirebaseDatabase.getInstance().getReference(HIT_LISTS_NODE);
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.child(movieId).child(SEEN_BY_NODE).child(currentUser.getUid()).removeValue();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int seenCount = dataSnapshot.child(movieId).child(SEEN_COUNT).getValue(Integer.class);
                if (seenCount == 1) {
                    databaseReference.child(movieId).removeValue();
                    listDatabase.child(currentUser.getUid()).child(movieId).removeValue();

                } else {
                    seenCount = seenCount - 1;
                    databaseReference.child(movieId).child(SEEN_COUNT).setValue(seenCount);
                }
                movieListAdapterCallback.onMovieSeen(seenCount, holder, position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSeenCount(final Movie movie, MovieDetailsPresenterCallback movieDetailsCallback) {
        this.movieDetailsPresenterCallback = movieDetailsCallback;
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String seenCount;
                if (dataSnapshot.hasChild(String.valueOf(movie.getId()))) {
                    seenCount = dataSnapshot.child(String.valueOf(movie.getId())).child(SEEN_COUNT).getValue().toString();
                } else {
                    seenCount = "0";
                }

                movieDetailsPresenterCallback.onSeenCountReceived(seenCount);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserDetails(final String uid, LoginPresenterCallback loginCallback) {
        this.loginPresenterCallback = loginCallback;
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(uid).child(EMAIL_NODE).getValue().toString();
                String firstName = dataSnapshot.child(uid).child(FIRST_NAME_NODE).getValue().toString();
                String lastName = dataSnapshot.child(uid).child(LAST_NAME_NODE).getValue().toString();
                String picture = dataSnapshot.child(uid).child(PICTURE_NODE).getValue().toString();
                User user = new User(firstName, lastName, email, picture);
                loginPresenterCallback.onUserDetailsRetrieved(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getWatchers(Movie movie, final ArrayList<Watcher> watcherList, WatchersPresenterCallback watchersCallback) {
        this.watchersPresenterCallback = watchersCallback;
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.child(String.valueOf(movie.getId())).child(SEEN_BY_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child(FULL_NAME_NODE).getValue().toString();
                    String picture = snapshot.child(PICTURE_NODE).getValue().toString();
                    String time = snapshot.child(TIME).getValue().toString();
                    Watcher watcher = new Watcher(name, picture, id, time);
                    watcherList.add(watcher);
                }
                watchersPresenterCallback.onWatchersReceived(watcherList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    public void searchForUser(final String userQuery, final ArrayList<User> usersList, UserSearchPresenterCallback userSearchCallback) {
        usersList.clear();
        this.userSearchPresenterCallback = userSearchCallback;
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child(EMAIL_NODE).getValue().toString().toLowerCase().contains(userQuery) ||
                            snapshot.child(FULL_NAME_NODE).getValue().toString().toLowerCase().contains(userQuery)) {
                        String id = snapshot.getKey();
                        String firstName = snapshot.child(FIRST_NAME_NODE).getValue().toString();
                        String lastName = snapshot.child(LAST_NAME_NODE).getValue().toString();
                        String email = snapshot.child(EMAIL_NODE).getValue().toString();
                        String picture = snapshot.child(PICTURE_NODE).getValue().toString();
                        User user = new User(firstName, lastName, email, picture);
                        user.setId(id);
                        usersList.add(user);
                    }

                }
                userSearchPresenterCallback.onUserSearchComplete(usersList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    public void getHitList(final ArrayList<HitMovie> hitList, final String uId, final HitListPresenterCallback hitListPresenterCallback) {
        databaseReference = FirebaseDatabase.getInstance().getReference(HIT_LISTS_NODE).child(uId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    HitMovie hitMovie = new HitMovie();
                    hitMovie.setMovie(movie);
                    hitList.add(hitMovie);
                }

                hitListPresenterCallback.onMoviesReceived(hitList, uId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSeenDate(final ArrayList<HitMovie> hitList, final HitListPresenterCallback hitListPresenter, final String uId) {
        databaseReference = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (HitMovie hitMovie : hitList) {
                    if (dataSnapshot.hasChild(String.valueOf(hitMovie.getMovie().getId()))) {
                        String seenDate = dataSnapshot.child(String.valueOf(hitMovie.getMovie().getId())).child(SEEN_BY_NODE).child(uId).child(TIME).getValue().toString();
                        hitMovie.setSeenDate(seenDate);
                    }
                }

                hitListPresenter.onHitListReceived(hitList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getWatcherEmail(final ArrayList<Watcher> watcherList, WatchersPresenterCallback watchersPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Watcher watcher : watcherList) {
                    if (dataSnapshot.hasChild(watcher.getId())) {
                        String email = dataSnapshot.child(watcher.getId()).child(EMAIL_NODE).getValue().toString();
                        watcher.setEmail(email);
                    }
                }

                watchersPresenterCallback.onFinalWatchersReceived(watcherList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void markAsFavourite(final Movie movie, MovieDetailsPresenterCallback movieDetailsPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(FAV_LISTS_NODE);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(movie.getId())).child(MOVIE_NODE).setValue(movie);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(movie.getId())).child(TIME).setValue(getCurrentDate());

        movieDetailsPresenter.onMovieMarkedAsFavourites();
    }

    public void checkIfMovieIsFavourite(final Movie movie, MovieDetailsPresenterCallback movieDetailsPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(FAV_LISTS_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild(String.valueOf(movie.getId()))) {
                        movieDetailsPresenterCallback.onFavouriteStatusReturn(true);
                    } else {
                        movieDetailsPresenterCallback.onFavouriteStatusReturn(false);
                    }
                } else {
                    movieDetailsPresenterCallback.onFavouriteStatusReturn(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeFromFavourite(Movie movie, MovieDetailsPresenterCallback movieDetailsPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(FAV_LISTS_NODE);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(movie.getId())).removeValue();

        movieDetailsPresenter.onMovieRemovedFromFavourites();
    }

    public void getFavList(final ArrayList<HitMovie> favList, String uid, final FavListPresenterCallback favListPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(FAV_LISTS_NODE).child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.child(MOVIE_NODE).getValue(Movie.class);
                    String time = snapshot.child(TIME).getValue().toString();

                    HitMovie hitMovie = new HitMovie();
                    hitMovie.setSeenDate(time);
                    hitMovie.setMovie(movie);

                    favList.add(hitMovie);

                }

                favListPresenter.onFavListRecieved(favList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPendingAndFriendsList(final ArrayList<User> pendingList, final ArrayList<User> friendsList, final String uId, final FriendsPresenterCallback friendsPresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Check for friends
                if (dataSnapshot.hasChild(FRIENDS_NODE) && dataSnapshot.child(FRIENDS_NODE).hasChild(uId)) {
                    friendsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child(FRIENDS_NODE).child(uId).getChildren()) {
                        User user = snapshot.getValue(User.class);
                        friendsList.add(user);
                    }
                }

                //Check for pending requests
                if (dataSnapshot.hasChild(PENDING_FRIENDS_NODE) && dataSnapshot.child(PENDING_FRIENDS_NODE).hasChild(uId)) {
                    for (DataSnapshot snapshot : dataSnapshot.child(PENDING_FRIENDS_NODE).child(uId).getChildren()) {
                        User user = snapshot.getValue(User.class);
                        pendingList.add(user);
                    }
                }

                friendsPresenter.onPendingAndFriendsReceived(pendingList, friendsList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendFriendRequest(final User user, UserListAdapter.MyViewHolder holder, int position, final User requestingUser, UserListAdapterCallback userListAdapter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(PENDING_FRIENDS_NODE);
        databaseReference.child(user.getId()).child(requestingUser.getId()).setValue(requestingUser);
        userListAdapter.onFriendRequestSent(user, holder, position, requestingUser);
    }

    public void sendFriendRequest(User user, User requestingUser, UserProfilePresenterCallback userProfilePresenter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(PENDING_FRIENDS_NODE);
        databaseReference.child(user.getId()).child(requestingUser.getId()).setValue(requestingUser);
        userProfilePresenter.onFriendRequestSent();
    }

    public void checkFriendButtonState(final User user, final UserListAdapter.MyViewHolder holder) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(PENDING_FRIENDS_NODE) && dataSnapshot.child(PENDING_FRIENDS_NODE).hasChild(user.getId())) {
                    if (dataSnapshot.child(PENDING_FRIENDS_NODE).child(user.getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.btnFriend.setText("Pending...");
                    } else {
                        holder.btnFriend.setText("Add Friend");
                    }
                }

                if (dataSnapshot.hasChild(FRIENDS_NODE) && dataSnapshot.child(FRIENDS_NODE).hasChild(user.getId())) {
                    if (dataSnapshot.child(FRIENDS_NODE).child(user.getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.btnFriend.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkFriendState(final Button btnFriend, final User user, User requestingUser) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(PENDING_FRIENDS_NODE) && dataSnapshot.child(PENDING_FRIENDS_NODE).hasChild(user.getId())) {
                    if (dataSnapshot.child(PENDING_FRIENDS_NODE).child(user.getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        btnFriend.setText("Pending...");
                    } else {
                        btnFriend.setText("Add Friend");
                    }
                }

                if (dataSnapshot.hasChild(FRIENDS_NODE) && dataSnapshot.child(FRIENDS_NODE).hasChild(user.getId())) {
                    if (dataSnapshot.child(FRIENDS_NODE).child(user.getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        btnFriend.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void acceptRequest(final User user, int position, User receivingUser, FriendRequestPendingAdapter.MyViewHolder holder, FriendRequestPendingAdapterCallback friendRequestPendingAdapter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(PENDING_FRIENDS_NODE).child(receivingUser.getId());
        databaseReference.child(user.getId()).removeValue();

        DatabaseReference friendsReference = FirebaseDatabase.getInstance().getReference(FRIENDS_NODE);
        friendsReference.child(receivingUser.getId()).child(user.getId()).setValue(user);
        friendsReference.child(user.getId()).child(receivingUser.getId()).setValue(receivingUser);

        friendRequestPendingAdapter.onRequestFinished(user, position, receivingUser, holder);
    }

    public void declineRequest(User user, int position, User receivingUser, FriendRequestPendingAdapter.MyViewHolder holder, FriendRequestPendingAdapterCallback friendRequestPendingAdapter) {
        databaseReference = FirebaseDatabase.getInstance().getReference(PENDING_FRIENDS_NODE).child(receivingUser.getId());
        databaseReference.child(user.getId()).removeValue();

        friendRequestPendingAdapter.onRequestFinished(user, position, receivingUser, holder);

    }
}
