package org.zerock.b3.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String memoText;

    //setter와 같지만 객체화시킨다. 객체지향에 맞춰서 만든다.
    //Entity객체도 변화해야 할 때가 있다.
    public void changeText(String text){
        this.memoText = text;
    }
}
