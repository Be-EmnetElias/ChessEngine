import pygame
import sys

from src.board import Board
from pygame import mixer
from src.helper import MoveType
from src.helper import GameState

class Main:

    def __init__(self):
        pygame.init()
        
        self.screen = pygame.display.set_mode((1000,800))
        pygame.display.set_caption("Chess")

        self.initial_position = "random_test"
        self.Board = Board(self.initial_position)

        # Create background
        bg_red = pygame.image.load("src//assets//imgs//bg_red.png")
        bg_red = pygame.transform.scale(bg_red,(800,800))

        bg_blue = pygame.image.load("src//assets//imgs//bg_blue.png")
        bg_blue = pygame.transform.scale(bg_blue,(800,800))

        bg_brown = pygame.image.load("src//assets//imgs//bg_brown.png")
        bg_brown = pygame.transform.scale(bg_brown,(800,800))

        bg_green = pygame.image.load("src//assets//imgs//bg_green.png")
        bg_green = pygame.transform.scale(bg_green,(800,800))
        

        self.bg_index = 0

        self.bg = {
            0: bg_red,
            1: bg_blue,
            2: bg_brown,
            3: bg_green
        }

        # Slice pieces.png into separate pieces
        pieces = pygame.image.load("src//assets//imgs//pieces.png")
        piece_width = pieces.get_width() // 6
        piece_height = pieces.get_height() // 2

        self.imgs = []

        self.outline_color = (255,255,255,100)
        self.highlight_color = (255,255,30,127)
        self.move_hint_color = (255,0,0,127)
        self.capture_color = (0,255,0,127)


        # Set up array of imgs
        for i in range(12):
            row = i // 6
            col = i % 6

            piece = pieces.subsurface(pygame.Rect(col * piece_width, row * piece_height, piece_width, piece_height))
            piece = pygame.transform.scale(piece,(100,100))

            self.imgs.append(piece)
        
        # Set up dictionary of move types to sounds
        self.sounds = {
            MoveType.DEFAULT: mixer.Sound("src//assets//sounds//DEFAULT.wav"),
            MoveType.ENPASSANT: mixer.Sound("src//assets//sounds//CAPTURE.wav"),
            MoveType.CAPTURE: mixer.Sound("src//assets//sounds//CAPTURE.wav"),
            MoveType.CASTLE_KING_SIDE: mixer.Sound("src//assets//sounds//CASTLE.wav"),
            MoveType.CASTLE_QUEEN_SIDE: mixer.Sound("src//assets//sounds//CASTLE.wav"),
            MoveType.PROMOTION: mixer.Sound("src//assets//sounds//PROMOTION.wav"),
            MoveType.CHECK: mixer.Sound("src//assets//sounds//CHECK.wav")
        }

        self.mainloop()


    def mainloop(self):
        # The piece that is being interacted with
        clicked_piece = None
        clicked_x_col, clicked_y_row = None, None
        previous_x_col, previous_y_row = None, None
        move_hints = None
        show_hint = True
        play_sound = True

        board = self.Board.board
        imgs = self.imgs

        font = pygame.font.Font('freesansbold.ttf', 10)
        white = (255, 255, 255)

        current_move = None
        drag_x, drag_y = 0, 0
        text = font.render(f"{drag_x} , {drag_y}", True, white)
        

        while True: # self.Board.GAME_STATE == GameState.ACTIVE:
            self.screen.fill((0,0,0))
            self.screen.blit(self.bg[self.bg_index],(0,0))
            self.screen.blit(text,(900,0))

            if drag_x and drag_y and self.Board.position_valid((drag_x//100,drag_y//100)):
                pygame.draw.rect(self.screen, self.outline_color, pygame.Rect(drag_x ,drag_y,100,100))

            if previous_x_col and previous_y_row:
                pygame.draw.rect(self.screen, self.highlight_color, pygame.Rect(previous_x_col,previous_y_row,100,100))

            if move_hints and show_hint and clicked_piece:
                for move in move_hints:
                    col, row = move.target_position
                    move_type = move.move_type
                    if self.Board.are_enemies(board[row][col],clicked_piece) or move_type == MoveType.ENPASSANT:
                        pygame.draw.rect(self.screen,self.capture_color,pygame.Rect(col*100,row*100,100,100))
                    else:
                        pygame.draw.rect(self.screen,self.move_hint_color,pygame.Rect(col*100,row*100,100,100))

            
            for event in pygame.event.get():
            
                clicked_x_col, clicked_y_row = pygame.mouse.get_pos()
                clicked_x_col = clicked_x_col//100 * 100
                clicked_y_row = clicked_y_row//100 * 100
                
                # keyboard press
                if event.type == pygame.KEYDOWN:

                    # click 'm' to toggle audio
                    if event.key == pygame.K_m:
                        play_sound = not play_sound
                        sound_status = "unmuted" if play_sound else "muted"
                        print(f"AUDIO {sound_status}")

                    # click 'h' to toggle hints
                    if event.key == pygame.K_h:
                        show_hint = not show_hint
                        hint_status = "showing hints" if show_hint else "hiding hints"
                        print(f"{hint_status}")

                    # click 'r' to reset board
                    if event.key == pygame.K_r:
                        self.Board.setBoard(self.initial_position)
                        print(f"Board reset to {self.initial_position}")

                    # click 'c' to change color
                    if event.key == pygame.K_c:
                        self.bg_index = (self.bg_index + 1) % len(self.bg)
                        print(f"Changed board color")

                    # click 'a' to go back 1 move
                    if event.key == pygame.K_a:
                        print("Undoing: ",end="")
                        for m in current_move:
                            print(m, end="")
                        print("\n")

                    # click 'd' to go forward 1 move
                    if event.key == pygame.K_d:
                        print(f"Going forward 1 move")

                    # click 'f' to get fen string
                    if event.key == pygame.K_f:
                        print(f"FENSTRING: {self.Board.get_position_fenstring()}")

                    # click 'p' to print board
                    if event.key == pygame.K_p:
                        print(self.Board.print_board())

                # click
                if event.type == pygame.MOUSEBUTTONDOWN:

                    clicked_row = event.pos[1] // 100
                    clicked_col = event.pos[0] // 100

                    previous_x_col = clicked_col * 100
                    previous_y_row = clicked_row * 100

                    clicked_piece = board[clicked_row][clicked_col]
                    if clicked_piece:
                        move_hints = self.Board.legal_moves(clicked_piece,check_for_checkmate=True)
                    #print(f"{clicked_piece} was clicked at {clicked_col},{clicked_row}")
                
                # mouse motion
                elif event.type == pygame.MOUSEMOTION:
                    
                    drag_x = int(event.pos[0] //100 * 100)
                    drag_y = int(event.pos[1] // 100 * 100)

                    text = font.render(f"{drag_x} , {drag_y}", True, white)
                    
                    if clicked_piece:
                        clicked_piece.x = event.pos[0] - 40
                        clicked_piece.y = event.pos[1] - 50

                    


                # release click
                elif clicked_piece and event.type == pygame.MOUSEBUTTONUP:
                    x,y = event.pos
                    col, row = x//100, y//100

                    # user_valid_move calls move_piece which also updates pixel location
                    valid, move, extra_move_type = self.Board.user_valid_move(clicked_piece,(col,row))

                    if valid: # user_valid move will move the piece automatically (should change)
                        
                        current_move = self.Board.move_piece(clicked_piece,move, simulation=False)
                        
                        if play_sound:
                            self.play_sound(extra_move_type)

                        move_hints = None

                    else: # Reset Piece
                        clicked_piece.x = clicked_piece.col * 100
                        clicked_piece.y = clicked_piece.row * 100
                        

                    clicked_piece = None
                    
                    previous_x_col = col * 100
                    previous_y_row = row * 100

                # quit
                elif event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

            for row in range(8):
                for col in range(8):
                    if board[row][col]:
                        piece = board[row][col]
                        index = piece.img_index
                        if piece != clicked_piece:
                            self.screen.blit(imgs[index],(piece.x,piece.y))

            if clicked_piece:
                self.screen.blit(imgs[clicked_piece.img_index],(clicked_piece.x,clicked_piece.y))

            pygame.display.update()

    def play_sound(self, move_type: MoveType) -> None:
        self.sounds[move_type].play()

