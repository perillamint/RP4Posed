# RP4Posed
Ridi Paper 4 Xposed 기능 개선 확장

# 이건 뭔가요
Ridi Paper 4 를 루팅(탈옥) 했을 때, ADB 할성화 및, 상태바 버튼 리매핑을 해주는 모듈입니다.

# 어떻게 쓰나요?

1. 리디 페이퍼 4 에 초기 탈옥용 커스텀 Magisk 와 LSPosed 를 잘(tm) 설치합니다. 해당 방법은 이 문서에서 다루지 않습니다.
2. 리디 페이퍼 4에 ADB 를 어떤 형태로든 연결한 뒤, 이 모듈을 설치합니다.
3. `adb shell am broadcast -a android.provider.Telephony.SECRET_CODE -d android_secret_code://5776733` 명령을 통해, LSPosed 를 실행합니다.
4. 모듈을 활성화 합니다. 모듈의 주입 범위에는 `System framework`, `SystemUI`, `com.eroum.service.RIDI` 가 반드시 포함되어야 합니다.
5. ???
6. PROFIT!
